#!/bin/bash

if [ $# -eq 0 ]; then
  DATA_DIR="./"
else
  DATA_DIR="$1"
fi

# Install Python dependencies
python3 -m pip install pip --upgrade
python3 -m pip install -r requirements.txt

# Download TF Lite models
FILE=deeplabv3.tflite
MODEL_URL='https://www.kaggle.com/api/v1/models/tensorflow/deeplabv3/tfLite/default/1/download'
if [ ! -f "$FILE" ]; then
  cd ${DATA_DIR}
  curl \
    -L ${MODEL_URL} \
    -o ${FILE}.tar.gz
  tar zxf ${FILE}.tar.gz
  mv 1.tflite ${FILE}
  /bin/rm ${FILE}.tar.gz
fi

echo -e "Downloaded files are in ${DATA_DIR}"
