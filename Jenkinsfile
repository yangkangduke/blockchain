// github 凭证
def git_auth = "2057a2c1-f936-4ebc-9467-cb23e5a96baa"
// 构建版本的名称
def tag = "latest"
// 镜像仓库地址
def harbor_url = "registry.cn-chengdu.aliyuncs.com/seeds-images"
def harbor_auth = "cb0581c9-9096-4c3e-9068-ea98e1b90917"
    node{
        stage('Git Checkout') {
               checkout([$class: 'GitSCM', branches: [[name: '*/${branch}']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: 'git@github.com:AllenAi007/seeds-java.git']]])
            }

        stage('Compile'){
            // 编译并安装
            sh "mvn clean install -pl ${project_name} -am -amd -Pdev -Dmaven.test.skip=true "
        }
        stage('Build Image'){
            //定义镜像名称
           def imageName = "${project_name}:${tag}"
           //编译，构建本地镜像
           sh "mvn -f ${project_name} clean package dockerfile:build"
            // 给镜像打标签
           sh "docker tag ${imageName} ${harbor_url}/${imageName}"
           //登录Harbor，并上传镜像

           withCredentials([usernamePassword(credentialsId: "${harbor_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {
               //登录
               sh "docker login -u ${username} -p ${password} ${harbor_url}"
               //上传镜像
               sh "docker push ${harbor_url}/${imageName}"
           }
            //删除本地镜像
            sh "docker rmi -f ${imageName}"
            sh "docker rmi -f ${harbor_url}/${imageName}"

        }
        stage('Deploy'){
            sshPublisher(publishers: [sshPublisherDesc(configName: '192.168.1.100', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/seeds/script/deploy.sh $harbor_url $project_name $tag $port", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
        }
    }



