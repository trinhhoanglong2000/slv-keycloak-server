#!/bin/bash

# Enable strict error handling
set -e

# Define your modules
modules=("slv-keycloak-core" "slv-keycloak-rest" "slv-keycloak-ui")

# Destination folder
dest="keycloak-providers"

# Create destination folder if it doesn't exist
if [ ! -d "$dest" ]; then
    mkdir "$dest"
fi

# Loop through each module
for module in "${modules[@]}"; do
    echo "Processing module: $module"

    source="$module/target"
    pattern="com.soloval.tech.$module-*.jar"

    # Check if source folder exists
    if [ -d "$source" ]; then
        echo "Looking for $pattern in $source"
        for file in "$source"/$pattern; do
            if [ -f "$file" ]; then
                echo "Copying $file to $dest"
                cp "$file" "$dest/"
            else
                echo "No files matching $pattern found in $source"
            fi
        done
    else
        echo "Folder $source does not exist. Skipping."
    fi
done

echo "Done."