@echo off

set MAVEN_OPTS=%MAVEN_OPTS% -Xms256m -Xmx512m

echo '安装到本地仓库'
call mvn install:install-file -Dfile=./taobao-sdk-java.jar -DgroupId=com.taobao -DartifactId=sdk-java -Dversion=1.0.RELEASE -Dpackaging=jar

echo '执行前先修改domain为自己的私有仓库'
echo '上传到远程仓库,releases包不允许更新，第二次上传会报错，可以忽略'
call mvn deploy:deploy-file -DgroupId=com.taobao -DartifactId=sdk-java -Dversion=1.0.RELEASE -Dpackaging=jar -Dfile=./taobao-sdk-java.jar -Durl=http://domain/repository/maven-releases/ -DrepositoryId=maven-releases

pause