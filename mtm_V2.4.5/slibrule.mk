#------------------------------------------------------------------------
# Define the flags to the compiler.  
#------------------------------------------------------------------------
CFLAGS = -c -g -std=c89 -D_GNU_SOURCE -fpic ${DEBUG}

#
# The rule makes a shared library and puts it in ${SHARED_LIBRARY)
#
${SHARED_LIBRARY}:	${OBJECTS} 
			rm -f ${SHARED_LIBRARY}
			echo create ${SHARED_LIBRARY} 
			${LD} -shared -o ${SHARED_LIBRARY} $(OBJECTS) -lm

