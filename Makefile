JAVAC=javac
JAVAC_FLAGS=-Xlint

all: fetchUrl.java
	$(JAVAC) $(JAVAC_FLAGS) fetchUrl.java

clean:
	rm -f *.class
