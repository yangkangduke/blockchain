// github 凭证
def git_auth = "2057a2c1-f936-4ebc-9467-cb23e5a96baa"
// 构建版本的名称
def tag = "latest"
// 镜像仓库地址
def harbor_url = "registry.cn-chengdu.aliyuncs.com/seeds-images"

    node{
        stage('Git Checkout') {
               checkout([$class: 'GitSCM', branches: [[name: '*/${branch}']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: ${git_auth}, url: 'git@github.com:AllenAi007/seeds-java.git']]])
            }

        stage('Compile'){
            // 编译并安装
            sh "mvn clean install -pl ${project_name} -am -amd -Pdev -Dmaven.test.skip=true "
        }
        stage('build image'){
            //定义镜像名称
           def imageName = "${project_name}:${tag}"
           //编译，构建本地镜像
           sh "mvn -f ${project_name} clean package dockerfile:build"
          /*  // 给镜像打标签
           sh "docker tag ${imageName} ${harbor_url}/${imageName}" */

        }
        stage('deploy'){

        }
    }



