
export inputfile=$1

export outputfile="${inputfile%.pbf}.cleansed.pbf"

echo cleaning $inputfile to  $outputfile

 osmosis                                                                 \
  --rb ${inputfile}                                                      \
  --bb clipIncompleteEntities=true top=90 left=-180 bottom=-90 right=180 \
  --wb ${outputfile} 
