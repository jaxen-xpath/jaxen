<!DOCTYPE style-sheet PUBLIC "-//James Clark//DTD DSSSL Style Sheet//EN" [

<!ENTITY docbook.dsl 
        PUBLIC "-//Norman Walsh//DOCUMENT DocBook HTML Stylesheet//EN" 
        CDATA DSSSL>

<!ENTITY dbstyle SYSTEM "/usr/local/docbook/stylesheets/dsssl/html/docbook.dsl" CDATA DSSSL>

]>

<style-sheet>
<style-specification use="docbook">
<style-specification-body>

	(define %generate-set-toc% 
		#t)

	(define %generate-book-toc% 
		#t)

	(define %stylesheet% 
		"style.css")

	(define %chapter-autolabel% 
		#t)

	(define %section-autolabel% 
		#t)

	(define %gentext-nav-use-tables%
		#t)

	(define %generate-book-titlepage%
		#t)

</style-specification-body>
</style-specification>
<external-specification id="docbook" document="docbook.dsl">
</style-sheet>
