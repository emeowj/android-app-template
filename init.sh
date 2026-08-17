#!/bin/bash

# Android Template Init Script
#
# One-time bootstrap for a fresh clone:
#   1. Renames the package from com.template to one you choose
#   2. Sets the app display name
#   3. Sets up the release signing keystore (generate new / use existing / skip)
#   4. Writes signing credentials to local.properties
#   5. Deletes itself on success
#
# Usage: ./init.sh

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

OLD_PACKAGE="com.template"
OLD_APP_CLASS="TemplateApplication"
OLD_APP_LABEL="Template"
OLD_THEME="TemplateTheme"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_DIR="$SCRIPT_DIR/app"
SIGNING_DIR="$APP_DIR/signing"
KEYSTORE_DEST="$SIGNING_DIR/release.keystore"
LOCAL_PROPS="$SCRIPT_DIR/local.properties"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Android Template Init${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# ---------------------------------------------------------------------------
# Step 1: collect package name
# ---------------------------------------------------------------------------
read -r -p "New package name (e.g. studio.supermiao.myapp): " NEW_PACKAGE
if [ -z "$NEW_PACKAGE" ]; then
    echo -e "${RED}Aborted: package name is required.${NC}"
    exit 1
fi
if ! [[ "$NEW_PACKAGE" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$ ]]; then
    echo -e "${RED}Invalid package name. Must be lowercase, dot-separated, at least two segments.${NC}"
    echo -e "${RED}Example: com.example.myapp${NC}"
    exit 1
fi

LAST_SEGMENT="${NEW_PACKAGE##*.}"
NEW_APP_CLASS="$(echo "${LAST_SEGMENT:0:1}" | tr '[:lower:]' '[:upper:]')${LAST_SEGMENT:1}Application"
NEW_THEME="${NEW_APP_CLASS%Application}Theme"

# ---------------------------------------------------------------------------
# Step 2: collect app display name
# ---------------------------------------------------------------------------
read -r -p "App display name (shown on launcher, default: $LAST_SEGMENT): " NEW_APP_LABEL
if [ -z "$NEW_APP_LABEL" ]; then
    NEW_APP_LABEL="$LAST_SEGMENT"
fi

# ---------------------------------------------------------------------------
# Step 3: signing setup
# ---------------------------------------------------------------------------
echo ""
echo "Release signing setup:"
echo "  1) Generate a new keystore now (recommended for new projects)"
echo "  2) Use an existing keystore"
echo "  3) Skip — release builds will fail until you set this up later"
read -r -p "Choose [1/2/3]: " SIGN_MODE

KEYSTORE_PASSWORD=""
KEYSTORE_CONFIGURED="no"

case "$SIGN_MODE" in
    1)
        mkdir -p "$SIGNING_DIR"
        if [ -f "$KEYSTORE_DEST" ]; then
            echo -e "${RED}A keystore already exists at $KEYSTORE_DEST. Refusing to overwrite.${NC}"
            exit 1
        fi
        read -r -p "Key alias (default: release): " KEY_ALIAS
        KEY_ALIAS="${KEY_ALIAS:-release}"

        while true; do
            read -r -s -p "Keystore password (min 6 chars): " KEYSTORE_PASSWORD
            echo ""
            if [ "${#KEYSTORE_PASSWORD}" -lt 6 ]; then
                echo -e "${RED}Password must be at least 6 characters.${NC}"
                continue
            fi
            read -r -s -p "Confirm password: " CONFIRM
            echo ""
            if [ "$KEYSTORE_PASSWORD" != "$CONFIRM" ]; then
                echo -e "${RED}Passwords don't match.${NC}"
                continue
            fi
            break
        done

        read -r -p "Validity in years (default: 25): " VALIDITY_YEARS
        VALIDITY_YEARS="${VALIDITY_YEARS:-25}"
        VALIDITY_DAYS=$((VALIDITY_YEARS * 365))

        read -r -p "Your name or organization (CN for the certificate): " CN
        if [ -z "$CN" ]; then
            CN="$NEW_APP_LABEL"
        fi

        if ! command -v keytool >/dev/null 2>&1; then
            echo -e "${RED}keytool not found on PATH. Install a JDK and retry.${NC}"
            exit 1
        fi

        echo -e "${YELLOW}Generating keystore at $KEYSTORE_DEST...${NC}"
        keytool -genkeypair -v \
            -keystore "$KEYSTORE_DEST" \
            -alias "$KEY_ALIAS" \
            -keyalg RSA -keysize 2048 \
            -validity "$VALIDITY_DAYS" \
            -storepass "$KEYSTORE_PASSWORD" \
            -keypass "$KEYSTORE_PASSWORD" \
            -dname "CN=$CN" \
            >/dev/null
        echo -e "${GREEN}  ✓ Keystore created${NC}"
        if [ "$KEY_ALIAS" != "release" ]; then
            echo -e "${YELLOW}  Note: alias is '$KEY_ALIAS' but app/build.gradle.kts uses 'release'.${NC}"
            echo -e "${YELLOW}  Update keyAlias in the signingConfigs block to match.${NC}"
        fi
        KEYSTORE_CONFIGURED="yes"
        ;;
    2)
        read -r -p "Path to existing keystore: " EXISTING_KEYSTORE
        EXISTING_KEYSTORE="${EXISTING_KEYSTORE/#\~/$HOME}"
        if [ ! -f "$EXISTING_KEYSTORE" ]; then
            echo -e "${RED}File not found: $EXISTING_KEYSTORE${NC}"
            exit 1
        fi
        read -r -p "Key alias in that keystore (default: release): " KEY_ALIAS
        KEY_ALIAS="${KEY_ALIAS:-release}"
        read -r -s -p "Keystore password: " KEYSTORE_PASSWORD
        echo ""
        if [ -z "$KEYSTORE_PASSWORD" ]; then
            echo -e "${RED}Password is required.${NC}"
            exit 1
        fi
        mkdir -p "$SIGNING_DIR"
        cp "$EXISTING_KEYSTORE" "$KEYSTORE_DEST"
        echo -e "${GREEN}  ✓ Copied keystore to $KEYSTORE_DEST${NC}"
        if [ "$KEY_ALIAS" != "release" ]; then
            echo -e "${YELLOW}  Note: alias is '$KEY_ALIAS' but app/build.gradle.kts uses 'release'.${NC}"
            echo -e "${YELLOW}  Update keyAlias in the signingConfigs block to match.${NC}"
        fi
        KEYSTORE_CONFIGURED="yes"
        ;;
    3)
        echo -e "${YELLOW}Skipping signing setup. See README ('Release builds') to configure later.${NC}"
        ;;
    *)
        echo -e "${RED}Invalid choice.${NC}"
        exit 1
        ;;
esac

# ---------------------------------------------------------------------------
# Step 4: write local.properties
# ---------------------------------------------------------------------------
if [ "$KEYSTORE_CONFIGURED" = "yes" ]; then
    touch "$LOCAL_PROPS"
    # Strip any pre-existing release-signing entries before appending.
    if grep -qE '^(RELEASE_KEYSTORE_PATH|RELEASE_KEY_PASSWORD)=' "$LOCAL_PROPS" 2>/dev/null; then
        grep -vE '^(RELEASE_KEYSTORE_PATH|RELEASE_KEY_PASSWORD)=' "$LOCAL_PROPS" > "$LOCAL_PROPS.tmp"
        mv "$LOCAL_PROPS.tmp" "$LOCAL_PROPS"
    fi
    # Ensure the file ends with a newline so our keys land on their own line.
    if [ -s "$LOCAL_PROPS" ] && [ "$(tail -c1 "$LOCAL_PROPS"; echo x)" != $'\nx' ]; then
        echo "" >> "$LOCAL_PROPS"
    fi
    {
        echo "RELEASE_KEYSTORE_PATH=signing/release.keystore"
        echo "RELEASE_KEY_PASSWORD=$KEYSTORE_PASSWORD"
    } >> "$LOCAL_PROPS"
    echo -e "${GREEN}  ✓ Wrote signing credentials to local.properties${NC}"
fi

# ---------------------------------------------------------------------------
# Step 5: rename package
# ---------------------------------------------------------------------------
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Renaming template${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "Package:      ${YELLOW}$OLD_PACKAGE${NC} -> ${YELLOW}$NEW_PACKAGE${NC}"
echo -e "App class:    ${YELLOW}$OLD_APP_CLASS${NC} -> ${YELLOW}$NEW_APP_CLASS${NC}"
echo -e "Theme:        ${YELLOW}$OLD_THEME${NC} -> ${YELLOW}$NEW_THEME${NC}"
echo -e "Display name: ${YELLOW}$OLD_APP_LABEL${NC} -> ${YELLOW}$NEW_APP_LABEL${NC}"
echo ""

OLD_PATH=$(echo "$OLD_PACKAGE" | tr '.' '/')
NEW_PATH=$(echo "$NEW_PACKAGE" | tr '.' '/')

echo -e "${YELLOW}Updating app/build.gradle.kts...${NC}"
sed -i '' "s/namespace = \"$OLD_PACKAGE\"/namespace = \"$NEW_PACKAGE\"/" "$APP_DIR/build.gradle.kts"
sed -i '' "s/applicationId = \"$OLD_PACKAGE\"/applicationId = \"$NEW_PACKAGE\"/" "$APP_DIR/build.gradle.kts"
echo -e "${GREEN}  ✓ Updated app/build.gradle.kts${NC}"

echo -e "${YELLOW}Updating settings.gradle.kts...${NC}"
sed -i '' "s/rootProject.name = \"android-app-template\"/rootProject.name = \"$NEW_APP_LABEL\"/" "$SCRIPT_DIR/settings.gradle.kts"
echo -e "${GREEN}  ✓ Updated settings.gradle.kts${NC}"

echo -e "${YELLOW}Updating package declarations and imports...${NC}"
update_source_files() {
    local src_dir="$1"
    if [ -d "$src_dir" ]; then
        find "$src_dir" -type f \( -name "*.kt" -o -name "*.java" \) | while read -r file; do
            sed -i '' "s/^package $OLD_PACKAGE/package $NEW_PACKAGE/" "$file"
            sed -i '' "s/import $OLD_PACKAGE/import $NEW_PACKAGE/g" "$file"
            sed -i '' "s/$OLD_APP_CLASS/$NEW_APP_CLASS/g" "$file"
            sed -i '' "s/$OLD_THEME/$NEW_THEME/g" "$file"
            echo -e "${GREEN}  ✓ ${file#$SCRIPT_DIR/}${NC}"
        done
    fi
}
update_source_files "$APP_DIR/src/main/java"
update_source_files "$APP_DIR/src/test/java"
update_source_files "$APP_DIR/src/androidTest/java"

echo -e "${YELLOW}Updating AndroidManifest.xml...${NC}"
MANIFEST_FILE="$APP_DIR/src/main/AndroidManifest.xml"
if [ -f "$MANIFEST_FILE" ]; then
    sed -i '' "s/android:name=\"\.$OLD_APP_CLASS\"/android:name=\".$NEW_APP_CLASS\"/" "$MANIFEST_FILE"
    sed -i '' "s/android:label=\"$OLD_APP_LABEL\"/android:label=\"$NEW_APP_LABEL\"/" "$MANIFEST_FILE"
    echo -e "${GREEN}  ✓ Updated AndroidManifest.xml${NC}"
fi

echo -e "${YELLOW}Updating strings.xml app_name...${NC}"
STRINGS_FILE="$APP_DIR/src/main/res/values/strings.xml"
if [ -f "$STRINGS_FILE" ]; then
    sed -i '' "s|<string name=\"app_name\">$OLD_APP_LABEL</string>|<string name=\"app_name\">$NEW_APP_LABEL</string>|" "$STRINGS_FILE"
    echo -e "${GREEN}  ✓ Updated strings.xml${NC}"
fi

echo -e "${YELLOW}Renaming Application class...${NC}"
OLD_APP_FILE="$APP_DIR/src/main/java/$OLD_PATH/$OLD_APP_CLASS.kt"
NEW_APP_FILE="$APP_DIR/src/main/java/$OLD_PATH/$NEW_APP_CLASS.kt"
if [ -f "$OLD_APP_FILE" ]; then
    mv "$OLD_APP_FILE" "$NEW_APP_FILE"
    echo -e "${GREEN}  ✓ $OLD_APP_CLASS.kt -> $NEW_APP_CLASS.kt${NC}"
fi

echo -e "${YELLOW}Moving source directories...${NC}"
move_source_dir() {
    local base_dir="$1"
    local old_dir="$base_dir/$OLD_PATH"
    local new_dir="$base_dir/$NEW_PATH"

    if [ -d "$old_dir" ]; then
        mkdir -p "$new_dir"
        if [ "$(ls -A "$old_dir")" ]; then
            cp -R "$old_dir"/* "$new_dir"/
        fi
        local old_root="${OLD_PATH%%/*}"
        local new_root="${NEW_PATH%%/*}"
        if [ "$old_root" != "$new_root" ]; then
            rm -rf "$base_dir/$old_root"
        else
            rm -rf "$old_dir"
            local parent_dir="$(dirname "$old_dir")"
            while [ "$parent_dir" != "$base_dir/$old_root" ] && [ -d "$parent_dir" ] && [ -z "$(ls -A "$parent_dir")" ]; do
                rmdir "$parent_dir"
                parent_dir="$(dirname "$parent_dir")"
            done
        fi
        echo -e "${GREEN}  ✓ $OLD_PATH -> $NEW_PATH in ${base_dir#$SCRIPT_DIR/}${NC}"
    fi
}
move_source_dir "$APP_DIR/src/main/java"
move_source_dir "$APP_DIR/src/test/java"
move_source_dir "$APP_DIR/src/androidTest/java"

echo -e "${YELLOW}Cleaning build caches...${NC}"
rm -rf "$APP_DIR/build" "$SCRIPT_DIR/build" "$SCRIPT_DIR/.gradle"
echo -e "${GREEN}  ✓ Cleaned${NC}"

# ---------------------------------------------------------------------------
# Step 6: self-destruct
# ---------------------------------------------------------------------------
echo -e "${YELLOW}Removing init script...${NC}"

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Done!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Next:"
echo "  1. Open the project in Android Studio and sync Gradle."
echo "  2. Replace the launcher icons in app/src/main/res/mipmap-* (release)"
echo "     and app/src/debug/res/mipmap-* (debug). See README for details."
if [ "$KEYSTORE_CONFIGURED" = "yes" ]; then
    echo "  3. Build a signed release: ./gradlew assembleRelease"
else
    echo "  3. Set up release signing later — see the README 'Release builds' section."
fi
echo ""

# Self-delete on success.
rm -- "$0"
