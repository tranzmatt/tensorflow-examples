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
FILE=${DATA_DIR}/posenet.tflite
if [ ! -f "$FILE" ]; then
  curl \
    -L 'https://storage.googleapis.com/download.tensorflow.org/models/tflite/posenet_mobilenet_v1_100_257x257_multi_kpt_stripped.tflite' \
    -o ${FILE}
fi

#curl -L -o ~/Downloads/model.tar.gz\
#  https://www.kaggle.com/api/v1/models/google/movenet/tfLite/singlepose-lightning-tflite-float16/1/download


cd ${DATA_DIR}

FILE=movenet_lightning.tflite
MODEL_URL='https://www.kaggle.com/api/v1/models/google/movenet/tfLite/singlepose-lightning-tflite-float16/1/download'
if [ ! -f "$FILE" ]; then
  curl \
    -L ${MODEL_URL} \
    -o ${FILE}.tar.gz
  tar zxf ${FILE}.tar.gz
  mv 4.tflite ${FILE}
  /bin/rm ${FILE}.tar.gz
  chmod 644 ${FILE}
fi

#curl -L -o ~/Downloads/model.tar.gz\
#  https://www.kaggle.com/api/v1/models/google/movenet/tfLite/singlepose-thunder-tflite-float16/1/download

FILE=movenet_thunder.tflite
MODEL_URL='https://www.kaggle.com/api/v1/models/google/movenet/tfLite/singlepose-thunder-tflite-float16/1/download'
if [ ! -f "$FILE" ]; then
  curl \
    -L ${MODEL_URL} \
    -o ${FILE}.tar.gz
  tar zxf ${FILE}.tar.gz
  mv 4.tflite ${FILE}
  /bin/rm ${FILE}.tar.gz
  chmod 644 ${FILE}
fi

#curl -L -o ~/Downloads/model.tar.gz\
#  https://www.kaggle.com/api/v1/models/google/movenet/tfLite/multipose-lightning-tflite-float16/1/download

FILE=movenet_multipose.tflite
MODEL_URL='https://www.kaggle.com/api/v1/models/google/movenet/tfLite/multipose-lightning-tflite-float16/1/download'
if [ ! -f "$FILE" ]; then
  curl \
    -L ${MODEL_URL} \
    -o ${FILE}.tar.gz
  tar zxf ${FILE}.tar.gz
  mv 1.tflite ${FILE}
  /bin/rm ${FILE}.tar.gz
  chmod 644 ${FILE}
fi

FILE=classifier.tflite
if [ ! -f "$FILE" ]; then
  curl \
    -L 'https://storage.googleapis.com/download.tensorflow.org/models/tflite/pose_classifier/yoga_classifier.tflite' \
    -o ${FILE}
fi

echo -e "Downloaded files are in ${DATA_DIR}"
