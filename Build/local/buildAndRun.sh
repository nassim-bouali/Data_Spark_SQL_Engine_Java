# navigate to the project root directory
# cd /path/to/project
# bash Build/local/buildAndRun.sh /path/to/data/input

INPUT_RESOURCES_PATH=$1

# build docker image
docker build -t spark-sql-batch-processing-java -f Build/local/dockerfile .

# run docker container
docker run \
-v $INPUT_RESOURCES_PATH:/resources \
-it spark-sql-batch-processing-java \
spark-submit \
--conf "spark.driver.extraJavaOptions=-Dlog4j.configuration=file:/resources/log4j.properties" \
--conf "spark.executor.extraJavaOptions=-Dlog4j.configuration=file:/resources/log4j.properties" \
--class com.data.spark.application.Application \
--files /resources/log4j.properties \
/app/distribution/data-spark-sql-application.jar \
--inline \
--plan "/resources/configuration-deployment-csv-to-jdbc.json"
