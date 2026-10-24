# Release channels

The app has two update channels, selected in **Settings → Update channel**:

| Channel | Who it is for | What it checks |
|---|---|---|
| **Stable** (default) | normal users | `latest` releases only |
| **Release candidates (RC)** | testers | the newest `rc` prerelease **and** the latest stable release |

Normal users never see RC builds: RC tags are published as GitHub **prereleases**, and the release
workflow does not upload `latest.yml` for them. The RC channel instead sees an `rc.yml` asset in the
prerelease.

## Tag scheme

| Tag | Channel | GitHub release type |
|---|---|---|
| `v0.18.0` | `latest` | release |
| `v0.18.0-rc1`, `v0.18.0-rc2`, ... | `rc` | prerelease |
| `v0.18.0-beta1` | `beta` | prerelease |
| `v0.18.0-alpha1` | `alpha` | prerelease |

## Upgrade chain

1. Testers select **Release candidates (RC)** in Settings and restart.
2. `v0.18.0-rc1` is installed; the updater checks `rc` first and follows `rc2`, `rc3`, ...
3. `v0.18.0` is published as a normal stable release.
4. RC testers also check the stable channel. Because `0.18.0` is newer than
   `0.18.0-rcN`, they are upgraded to the final release.
5. Normal users on Stable only ever see `v0.18.0`.

This avoids publishing the final build to `rc.yml`; the version comparison handles the transition.

## Workflow details

- `release.yml` reads `-rc`, `-beta` and `-alpha` from the tag.
- Nucleus's Gradle `ReleaseChannel` enum only has `latest`, `beta`, `alpha`, so RC builds use
  `build_channel=beta` internally. The published channel remains `rc`.
- Nucleus's `generate-update-yml` action maps unknown channels to `latest.yml`, so the workflow
  renames `latest*.yml` to `rc*.yml` before publishing an RC. Do not remove that step unless the
  upstream action gains native RC support.
- RC users need the prerelease metadata to be reachable; the updater resolves the newest matching
  prerelease tag through the GitHub API.

## Release checklist

```powershell
# RC
git tag v0.18.0-rc1
git push origin v0.18.0-rc1

# next RC
git tag v0.18.0-rc2
git push origin v0.18.0-rc2

# final
git tag v0.18.0
git push origin v0.18.0
```

After the final release, RC testers can switch back to Stable in Settings; they will already be on
the same `0.18.0` build.
