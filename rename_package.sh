#!/bin/bash

# Android Package Renamer Script
# Renames the package from com.template to a new package name
# Usage: ./rename_package.sh <new_package_name>
# Example: ./rename_package.sh studio.supermiao.myapp

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Current package name and application class
OLD_PACKAGE="com.template"
OLD_APP_CLASS="TemplateApplication"

# Get script directory (project root)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_DIR="$SCRIPT_DIR/app"

# Validate input
if [ -z "$1" ]; then
    echo -e "${RED}Error: Please provide a new package name${NC}"
    echo "Usage: ./rename_package.sh <new_package_name>"
    echo "Example: ./rename_package.sh studio.supermiao.myapp"
    exit 1
fi

NEW_PACKAGE="$1"

# Validate package name format (should be like com.example.app)
if ! [[ "$NEW_PACKAGE" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$ ]]; then
    echo -e "${RED}Error: Invalid package name format${NC}"
    echo "Package name should be lowercase, start with a letter, and have at least two segments"
    echo "Example: com.example.myapp or studio.supermiao.myapp"
    exit 1
fi

# Derive Application class name from the last segment of the package
# e.g., studio.supermiao.myapp -> MyappApplication
LAST_SEGMENT="${NEW_PACKAGE##*.}"
# Capitalize first letter
NEW_APP_CLASS="$(echo "${LAST_SEGMENT:0:1}" | tr '[:lower:]' '[:upper:]')${LAST_SEGMENT:1}Application"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Android Package Renamer${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "Old package: ${YELLOW}$OLD_PACKAGE${NC}"
echo -e "New package: ${YELLOW}$NEW_PACKAGE${NC}"
echo -e "Old Application class: ${YELLOW}$OLD_APP_CLASS${NC}"
echo -e "New Application class: ${YELLOW}$NEW_APP_CLASS${NC}"
echo ""

# Convert package names to directory paths
OLD_PATH=$(echo "$OLD_PACKAGE" | tr '.' '/')
NEW_PATH=$(echo "$NEW_PACKAGE" | tr '.' '/')

echo -e "${YELLOW}Step 1: Updating build.gradle.kts...${NC}"

# Update namespace and applicationId in app/build.gradle.kts
sed -i '' "s/namespace = \"$OLD_PACKAGE\"/namespace = \"$NEW_PACKAGE\"/" "$APP_DIR/build.gradle.kts"
sed -i '' "s/applicationId = \"$OLD_PACKAGE\"/applicationId = \"$NEW_PACKAGE\"/" "$APP_DIR/build.gradle.kts"

echo -e "${GREEN}  ✓ Updated app/build.gradle.kts${NC}"

echo -e "${YELLOW}Step 2: Updating package declarations and imports in source files...${NC}"

# Function to update package declarations and imports in Kotlin/Java files
update_source_files() {
    local src_dir="$1"
    if [ -d "$src_dir" ]; then
        find "$src_dir" -type f \( -name "*.kt" -o -name "*.java" \) | while read -r file; do
            # Update package declaration
            sed -i '' "s/^package $OLD_PACKAGE/package $NEW_PACKAGE/" "$file"
            # Update imports
            sed -i '' "s/import $OLD_PACKAGE/import $NEW_PACKAGE/g" "$file"
            # Update Application class references
            sed -i '' "s/$OLD_APP_CLASS/$NEW_APP_CLASS/g" "$file"
            echo -e "${GREEN}  ✓ Updated: ${file#$SCRIPT_DIR/}${NC}"
        done
    fi
}

# Update source files in main, test, and androidTest
update_source_files "$APP_DIR/src/main/java"
update_source_files "$APP_DIR/src/test/java"
update_source_files "$APP_DIR/src/androidTest/java"

echo -e "${YELLOW}Step 3: Updating AndroidManifest.xml...${NC}"

# Update AndroidManifest.xml - change Application class reference
MANIFEST_FILE="$APP_DIR/src/main/AndroidManifest.xml"
if [ -f "$MANIFEST_FILE" ]; then
    sed -i '' "s/android:name=\"\.$OLD_APP_CLASS\"/android:name=\".$NEW_APP_CLASS\"/" "$MANIFEST_FILE"
    echo -e "${GREEN}  ✓ Updated AndroidManifest.xml${NC}"
fi

echo -e "${YELLOW}Step 4: Renaming Application class file...${NC}"

# Rename TemplateApplication.kt to new name
OLD_APP_FILE="$APP_DIR/src/main/java/$OLD_PATH/$OLD_APP_CLASS.kt"
NEW_APP_FILE="$APP_DIR/src/main/java/$OLD_PATH/$NEW_APP_CLASS.kt"
if [ -f "$OLD_APP_FILE" ]; then
    mv "$OLD_APP_FILE" "$NEW_APP_FILE"
    echo -e "${GREEN}  ✓ Renamed $OLD_APP_CLASS.kt -> $NEW_APP_CLASS.kt${NC}"
fi

echo -e "${YELLOW}Step 5: Moving source directories...${NC}"

# Function to move source directory
move_source_dir() {
    local base_dir="$1"
    local old_dir="$base_dir/$OLD_PATH"
    local new_dir="$base_dir/$NEW_PATH"

    if [ -d "$old_dir" ]; then
        # Create new directory structure
        mkdir -p "$new_dir"

        # Move all contents
        if [ "$(ls -A "$old_dir")" ]; then
            cp -R "$old_dir"/* "$new_dir"/
        fi

        # Remove old directory structure
        # Walk up from the old path and remove empty directories
        rm -rf "$base_dir/com"

        echo -e "${GREEN}  ✓ Moved: $OLD_PATH -> $NEW_PATH in ${base_dir#$SCRIPT_DIR/}${NC}"
    fi
}

# Move directories for main, test, and androidTest
move_source_dir "$APP_DIR/src/main/java"
move_source_dir "$APP_DIR/src/test/java"
move_source_dir "$APP_DIR/src/androidTest/java"

echo -e "${YELLOW}Step 6: Cleaning build directory...${NC}"

# Remove build directory to force regeneration
if [ -d "$APP_DIR/build" ]; then
    rm -rf "$APP_DIR/build"
    echo -e "${GREEN}  ✓ Removed app/build directory${NC}"
fi

if [ -d "$SCRIPT_DIR/build" ]; then
    rm -rf "$SCRIPT_DIR/build"
    echo -e "${GREEN}  ✓ Removed root build directory${NC}"
fi

# Also clean .gradle cache
if [ -d "$SCRIPT_DIR/.gradle" ]; then
    rm -rf "$SCRIPT_DIR/.gradle"
    echo -e "${GREEN}  ✓ Removed .gradle cache${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Package rename complete!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "Next steps:"
echo "  1. Open the project in Android Studio"
echo "  2. Sync Gradle files (File > Sync Project with Gradle Files)"
echo "  3. Build the project to verify everything works"
echo ""
echo -e "${YELLOW}Note: You may also want to update:${NC}"
echo "  - App name in app/src/main/res/values/strings.xml"
echo ""
