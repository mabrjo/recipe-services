ENVIRONMENT=$1
CONTAINER_REGISTRY=$2
CONTAINER_IMAGE=$3

CONTAINER_NAME="recipe-services-${ENVIRONMENT}"

if [[ ${ENVIRONMENT} == develop ]]
    then
            PORT=8088
    elif [[ ${ENVIRONMENT} == master ]]
    then
            PORT=8082
    else
            PORT=9999
fi

if [[ PORT != 9999 ]]
    then
        if [[ $(docker container ls -q --filter name=${CONTAINER_NAME}) ]];
                then
                        echo "found running container, killing thing"
                        docker stop ${CONTAINER_NAME}
        else
                echo "no running container, starting new one"
        fi
        aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin ${CONTAINER_REGISTRY}
        docker pull ${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}

        docker run -p ${PORT}:8080 --rm --env-file=${ENVIRONMENT}.env --mount source=dev-h2,target=/var/recipeDB --name ${CONTAINER_NAME} --detach ${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}
else
        echo "project not built on develop or master, not deploying docker"
fi