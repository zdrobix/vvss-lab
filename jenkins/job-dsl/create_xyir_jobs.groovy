// Job DSL script to create three pipeline jobs that load Jenkinsfiles from the repository
// Adjust `gitUrl` to your repository and `repoBranch` as needed before running in Jenkins seed job

def gitUrl = 'https://your.git.repo/your-repo.git' // <<< replace with your Git repo URL
def repoBranch = 'main'

pipelineJob('xyir1234Job_BBT') {
    definition {
        cpsScm {
            scm {
                git {
                    remote { url(gitUrl) }
                    branch(repoBranch)
                }
            }
            scriptPath('jenkins/jenkinsfiles/Jenkinsfile_BBT')
        }
    }
}

pipelineJob('xyir1234Job_WBT') {
    definition {
        cpsScm {
            scm {
                git {
                    remote { url(gitUrl) }
                    branch(repoBranch)
                }
            }
            scriptPath('jenkins/jenkinsfiles/Jenkinsfile_WBT')
        }
    }
}

pipelineJob('xyir1234Job_IntT') {
    definition {
        cpsScm {
            scm {
                git {
                    remote { url(gitUrl) }
                    branch(repoBranch)
                }
            }
            scriptPath('jenkins/jenkinsfiles/Jenkinsfile_IntT')
        }
    }
}
