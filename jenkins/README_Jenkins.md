Jenkins Automation for Lab04 - Quick Start

This folder contains Job DSL and Jenkinsfile examples to create three Jenkins jobs for the labs:

- xyir1234Job_BBT (Lab2 - Black-box tests)
- xyir1234Job_WBT (Lab3 - White-box tests)
- xyir1234Job_IntT (Lab4 - Integration tests)

Prerequisites on Jenkins:

- Git plugin
- Pipeline plugin
- JUnit plugin
- Job DSL plugin (optional, for seed job)
- TestLink plugin (optional, to publish results back to TestLink)
- Maven installed and configured in Jenkins global tools

Files added:

- jenkins/job-dsl/create_xyir_jobs.groovy : Job DSL script that creates three pipeline jobs pointing at the Jenkinsfiles in this repo
- jenkins/jenkinsfiles/Jenkinsfile_BBT : Declarative pipeline for BBT job
- jenkins/jenkinsfiles/Jenkinsfile_WBT : Declarative pipeline for WBT job
- jenkins/jenkinsfiles/Jenkinsfile_IntT : Declarative pipeline for IntT job

How to use (Job DSL seed job):

1. Create a new Freestyle job named `seed-job` in Jenkins.
2. Add a build step: Process Job DSLs -> Look on the workspace -> Provide `jenkins/job-dsl/create_xyir_jobs.groovy` path.
3. Configure Git SCM for seed job to point to this repository and branch.
4. Run the seed job. It will create three pipeline jobs on the Jenkins master.

How to run pipeline jobs (once created):

- Open job xyir1234Job_IntT and click "Build with Parameters".
- Set `TEST_CLASS` to the fully qualified JUnit class name or pattern (e.g. `IntegrationStep3_ServiceValidatorRepositoryTest`).
- Set TestLink parameters if plugin present.
- Click "Build".

How the pipeline maps to TestLink

- Pipelines run `mvn -Dtest=${TEST_CLASS} verify` and publish JUnit XML reports `target/surefire-reports/TEST-*.xml`.
- To let TestLink map JUnit results to TestLink Test Cases, ensure each Test Case in TestLink has Custom Fields `JavaClassName` and `JavaTestMethodName` (exact method name).
- Install and configure the TestLink plugin on Jenkins; then add a post-build step to publish results to TestLink. The plugin can map JUnit methods using "JUnit method name" strategy and the custom field key (JavaTestMethodName).

Manual Jenkins creation (UI):

1. New Item -> Name: xyir1234Job_IntT -> Choose Pipeline
2. In Pipeline definition choose "Pipeline script from SCM" -> Git -> Repository URL -> Script Path: `jenkins/jenkinsfiles/Jenkinsfile_IntT`
3. Add any credentials, branches as needed.
4. Save and click Build with Parameters.

Notes & Limitations

- The Job DSL script contains a placeholder `gitUrl` value. Replace with your Git repository URL before running.
- The Jenkinsfiles include commented examples of TestLink plugin usage. The exact invocation depends on your TestLink plugin installation and version.
- If your Jenkins runs on Windows agents, the pipeline uses `bat` instead of `sh` automatically.

Troubleshooting

- If JUnit reports are not published, ensure `target/surefire-reports/TEST-*.xml` files exist after Maven execution.
- If TestLink mapping fails, confirm that Custom Field values in TestLink exactly match `JavaClassName` and `JavaTestMethodName` from your test classes.

Examples of commands (seed job alternative using Jenkins CLI)

```bash
# Run on your machine (requires jenkins-cli.jar and Jenkins accessible)
java -jar jenkins-cli.jar -s http://jenkins.example.com/ create-job xyir1234Job_IntT < jenkins/job-dsl/create_xyir_jobs.groovy
```

Contact

- If you want, I can also generate a Jenkins seed job config XML or attempt to add TestLink configure snippets for a specific TestLink plugin version. Provide your Jenkins plugin versions and I will tailor the DSL accordingly.
