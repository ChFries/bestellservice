# bestellservice

## Build Project and Image

To build the project and the image docker is required

If you have Maven installed run

```bash
./buildProjectAndImage.sh
```

If not run

```bash
./buildProjectAndImageMvnw.sh
```

This uses the included mavenwrapper. 

It might be necessary to change permissions of the scripts. To do so run

```bash
chmod 777 buildProjectAndImage.sh
chmod 777 buildProjectAndImageMvnw.sh
chmod 777 mvnw
```