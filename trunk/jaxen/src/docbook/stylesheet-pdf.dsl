<!DOCTYPE style-sheet PUBLIC "-//James Clark//DTD DSSSL Style Sheet//EN" [

	<!ENTITY docbook.dsl 
	        PUBLIC "-//Norman Walsh//DOCUMENT DocBook Print Stylesheet//EN" 
	        CDATA DSSSL>

]>

<style-sheet>
<style-specification use="docbook">
<style-specification-body>

(declare-characteristic heading-level 
   "UNREGISTERED::James Clark//Characteristic::heading-level" 3)

	(define %generate-heading-level% 
		#t)

        (define %generate-set-toc% 
		#t)

        (define %generate-book-toc% 
		#t)

	(define (toc-depth nd) 
		5)

        (define %chapter-autolabel% 
		#t)

        (define %section-autolabel% 
		#t)

	(define %top-margin%
		3pi)

	(define %bottom-margin%
		3pi)

	(define %left-margin%
		3pi)

	(define %right-margin%
		6pi)

	(define %para-indent%
  		0pi)

	(define %para-indent-firstpara%
  		0pi)

	(define %titlepage-n-columns%
  		1)

	(define %body-start-indent% 
		1pi)

</style-specification-body>
</style-specification>
<external-specification id="docbook" document="docbook.dsl">
</style-sheet>
