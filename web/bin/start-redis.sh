#!/usr/bin/env bash

sudo docker run --name redis_freemoney_dev -p 6379:6379 -d redis || sudo docker restart redis_freemoney_dev

