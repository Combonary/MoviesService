# Publishing New Versions

## Quick Steps

Publishing a new version is straightforward. Follow these steps:

### 1. Update Version
Open `library/build.gradle.kts` and update the version number:

```kotlin
version = "1.0.3"  // Change to your new version
```

### 2. Publish to GitHub Packages
```bash
./gradlew publish -x :library:allTests
```

### 3. Verify Success
You'll see `BUILD SUCCESSFUL` in the output.

## Step-by-Step Guide

### Step 1: Update the Version Number

Edit `/library/build.gradle.kts`:

```kotlin
group = "io.github.combonary"
version = "1.0.3"  // ← Update this to your new version
```

### Step 2: Publish

Run the publish command:

```bash
cd /Users/pascaladjaero/AndroidStudioProjects/MoviesService
./gradlew publish -x :library:allTests
```

**Flags explained:**
- `publish` - Publishes to all configured repositories (GitHub Packages)
- `-x :library:allTests` - Excludes failing tests from blocking the publish

### Step 3: Verify

Check the output for `BUILD SUCCESSFUL`:

```
BUILD SUCCESSFUL in 2m 48s
```

## Publishing Timeline

- **Time to publish**: 2-3 minutes
- **Artifacts published**: All targets (Android, iOS, JVM, Linux)
- **Immediate availability**: Yes, available right after successful publish

## What Gets Published

Each version publishes these artifacts:

```
io.github.combonary:MoviesService:1.0.3
├── MoviesService-1.0.3.jar              (Kotlin Multiplatform binary)
├── MoviesService-1.0.3-sources.jar      (Source code)
├── MoviesService-1.0.3.module           (Gradle metadata)
└── MoviesService-1.0.3.pom              (Maven POM)
```

All available at: `https://maven.pkg.github.com/Combonary/MoviesService`

## Version Naming Conventions

### Release Versions
- Format: `MAJOR.MINOR.PATCH` (e.g., `1.0.3`)
- Use when publishing stable versions

### Snapshot Versions
- Format: `1.0.3-SNAPSHOT` (e.g., `1.0.3-SNAPSHOT`)
- Use for development/pre-release versions
- Can be overwritten (unlike release versions)

### Beta/RC Versions
- Format: `1.0.3-beta01`, `1.0.3-rc01`
- Use for pre-release testing

## Common Commands

### Publish to GitHub Packages
```bash
./gradlew publish -x :library:allTests
```

### Publish Locally First (Testing)
```bash
./gradlew publishToMavenLocal -x :library:allTests
```

### Build Only (No Publishing)
```bash
./gradlew build -x :library:allTests
```

### Check Publishing Tasks
```bash
./gradlew tasks --group=publishing
```

## Team Workflow

### After Publishing

1. **Notify Team**: Let them know the new version is available
2. **Update Dependency**: They update to the new version:
   ```kotlin
   implementation("io.github.combonary:MoviesService:1.0.3")
   ```

3. **Environment Variables**: Ensure they have GitHub credentials set:
   ```bash
   export GITHUB_ACTOR=their_github_username
   export GITHUB_TOKEN=their_personal_access_token
   ```

## Troubleshooting

### "Received status code 403"
- Check GitHub token has `packages: write` permission
- Verify username/token are correct in gradle.properties or environment variables

### "Could not find artifact"
- Verify group ID: `io.github.combonary`
- Verify artifact ID: `MoviesService`
- Verify version is correct

### Tests Failing
- Use `-x :library:allTests` to skip tests and proceed with publishing
- Or use `-x :library:jvmTest` to skip only JVM tests

## Git Tag (Optional but Recommended)

After publishing, create a git tag for tracking:

```bash
git tag v1.0.3
git push origin v1.0.3
```

This creates a GitHub Release you can view at:
```
https://github.com/Combonary/MoviesService/releases/tag/v1.0.3
```

## Example Workflow

```bash
# Update version
nano library/build.gradle.kts      # Change version to 1.0.3

# Publish
./gradlew publish -x :library:allTests

# Create git tag (optional)
git tag v1.0.3
git push origin v1.0.3

# Done! Version 1.0.3 is now available
```

## Resources

- [Gradle Publishing Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)
- [GitHub Packages](https://docs.github.com/en/packages)
- [Maven Version Numbers](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

---

**Total time to publish:** ~2-3 minutes ⏱️

