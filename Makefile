# Manelisi Luthuli
# 06 September 2024

BIN = bin
SRC = src
LIB = lib

# Explicitly list all JAR files in the lib directory
JARS = $(LIB)/commons-collections4-4.4.jar:$(LIB)/commons-compress-1.21.jar:$(LIB)/json-20210307.jar:$(LIB)/poi-4.1.2.jar:$(LIB)/poi-ooxml-4.1.2.jar:$(LIB)/poi-ooxml-schemas-4.1.2.jar:$(LIB)/xmlbeans-3.1.0.jar

# Classpath includes both the bin directory and all the JAR files
CLASSPATH = $(BIN):$(JARS)

# List of Java files to compile
ALL = Shuffler RandomArrayIndexes ArrayGeneration Data

# Rule to compile .java files to .class files
$(BIN)/%.class: $(SRC)/%.java
	@mkdir -p $(BIN)
	javac -d $(BIN) -cp "$(CLASSPATH)" $<

# Default target
default: $(ALL:%=$(BIN)/%.class)

# Run the main program (Data class in this case)
run:
	java -cp "$(CLASSPATH)" Data

# Clean up .class files
clean:
	rm -rf $(BIN)/*.class

