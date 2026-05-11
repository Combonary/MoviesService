# Automated Publishing via GitHub Actions

## Overview
Your library now automatically publishes to GitHub Packages when you create a GitHub release!

## How It Works

### Workflow Trigger
The `publish.yml` workflow is triggered whenever you:
1. Create a new GitHub release in the repository
2. Mark it as "Published" (not draft)

### Automatic Steps
When triggered, the workflow:
1. ✅ Checks out your code
2. ✅ Sets up Java 17
3. ✅ Runs: `./gradlew publish -x :library:allTests`
4. ✅ Publishes all artifacts to GitHub Packages

### Environment Variables
The workflow automatically provides:
- `GITHUB_ACTOR` - Your GitHub username
- `GITHUB_TOKEN` - GitHub's built-in token with package write permissions

## Publishing New Versions

### Step 1: Update Version
Edit `library/build.gradle.kts`:
```kotlin
version = "1.0.4"  // Your new version
```

### Step 2: Commit and Push
```bash
git add library/build.gradle.kts
git commit -m "Bump version to 1.0.4"
git push origin main
```

### Step 3: Create GitHub Release
Go to: `https://github.com/Combonary/MoviesService/releases/new`

Or use GitHub CLI:
```bash
git tag v1.0.4
git push origin v1.0.4
gh release create v1.0.4 --generate-notes
```

### Step 4: Mark as Published
In the GitHub UI:
- Click "Publish release" (not "Save as draft")
- The workflow automatically triggers! 🚀

## Workflow Automation Timeline

```
Update version in code
           ↓
    Commit & push
           ↓
  Create git tag (v1.0.4)
           ↓
Create GitHub Release
(click "Publish")
           ↓
   Workflow triggered
(automatic publishing)
           ↓
   Artifacts in GitHub
     Packages (~2 min)
```

## Monitoring the Workflow

### Watch Execution
1. Go to: `https://github.com/Combonary/MoviesService/actions`
2. Click on "Publish to GitHub Packages" workflow
3. Watch the latest run in real-time

### Workflow Status
- 🟢 **Green** = Published successfully
- 🔴 **Red** = Publishing failed (check logs)
- 🟡 **Yellow** = Running

### Check Published Artifacts
After the workflow completes, verify at:
```
https://github.com/Combonary/MoviesService/packages
```

## Example Workflow: Publishing v1.0.4

```bash
# 1. Update version
# Edit: library/build.gradle.kts
# Change: version = "1.0.4"

# 2. Commit and push
git add library/build.gradle.kts
git commit -m "Bump version to 1.0.4"
git push origin main

# 3. Create and push tag
git tag v1.0.4
git push origin v1.0.4

# 4. Create release (using GitHub CLI)
gh release create v1.0.4 --generate-notes

# 5. Workflow automatically runs (2-3 minutes)
# 6. Check GitHub Actions tab to monitor
# 7. Verify artifacts at GitHub Packages
```

## GitHub Web UI Method

### Alternative: Using Only GitHub Web UI
1. Go to: `https://github.com/Combonary/MoviesService/releases/new`
2. Fill in:
   - **Tag version:** `v1.0.4`
   - **Release title:** `Version 1.0.4`
   - **Description:** Add changes/features
3. Click "Publish release"
4. ✅ Workflow automatically runs!

**Note:** This assumes you've already pushed the version update to `main`

## Timing

| Step | Time |
|------|------|
| Release creation | Instant |
| Workflow trigger | Seconds |
| Build & publish | 2-3 minutes |
| Available on GitHub Packages | Immediate after build |
| Searchable in GitHub | Few minutes (indexing) |

## Troubleshooting

### Workflow Not Triggering
**Problem:** You created a release but workflow didn't run

**Solutions:**
1. Make sure the release is "Published" (not draft)
2. Check you created it as a "Release", not a "Tag"
3. Verify the workflow ran: Go to **Actions** tab

### Publishing Failed
**Problem:** Workflow ran but publishing failed

**Solutions:**
1. Click the failed workflow run
2. Check the error logs in "Publish to GitHub Packages" step
3. Common issues:
   - Invalid version format
   - GitHub token expired (usually not an issue with `${{ secrets.GITHUB_TOKEN }}`)
   - Network issues (retry the workflow)

### Retry Failed Workflow
If the workflow fails:
1. Go to: `https://github.com/Combonary/MoviesService/actions`
2. Find the failed workflow run
3. Click "Re-run failed jobs"
4. Workflow runs again

## Workflow File Reference

The workflow is defined in: `.github/workflows/publish.yml`

**Key configuration:**
- **Trigger:** `types: [published]` - Only published releases (not drafts)
- **OS:** `ubuntu-latest` - Lightweight and fast
- **Java:** 17 - Compatible with your build
- **Skip tests:** `-x :library:allTests` - Speed up publishing
- **Credentials:** GitHub's built-in `GITHUB_TOKEN` with package write permissions

## Next Steps

1. ✅ Update version in `library/build.gradle.kts`
2. ✅ Commit: `git add . && git commit -m "Bump version"`
3. ✅ Push: `git push origin main`
4. ✅ Create release on GitHub
5. ✅ Watch the workflow run automatically!

## Team Communication

When publishing a new version:

```markdown
# Version 1.0.4 Released! 🎉

New version is now available on GitHub Packages:

```kotlin
implementation("io.github.combonary:MoviesService:1.0.4")
```

Changes:
- Feature X
- Bug fix Y
- Performance improvement Z

Automated workflow took ~3 minutes to publish!
```

---

**Automation Status:** ✅ Live and Ready!

From now on, you just need to:
1. Update the version
2. Create a GitHub release
3. Everything else is automatic! 🚀

