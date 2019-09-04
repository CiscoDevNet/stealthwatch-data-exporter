# Make or update SVG diagrams from UML sources.

uml_sources = $(wildcard *.uml)
svg_diagrams = $(patsubst %.uml,%.svg,${uml_sources})
#png_diagrams = $(patsubst %.uml,%.png,${uml_sources})

build: ${svg_diagrams} ${png_diagrams}

clean:
	rm -f *.svg
	rm -f *.png

%.svg: %.uml
	plantuml -tsvg $<

%.png: %.uml
	plantuml -tpng $<
