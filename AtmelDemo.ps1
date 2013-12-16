
#Author:Dhananjay Ghevde



$RestAPIJavaDocGenerator = 'C:/Dev/Atmel/RestAPIJavaDocGenerator/converterutil.jar'
$Restapi =   'C:/Dev/Atmel/RestAPIJavaDocGenerator/restAPI.xsl'
$RestClass = 'C:/Dev/Atmel/RestAPIJavaDocGenerator/restClass.xsl'
$indir =     'C:/Dev/Atmel/RestAPIJavaDocGenerator/restAPIsrc'
$logdir =    'C:/Dev/Atmel/RestAPIJavaDocGenerator/logs'
$outdir =    'C:/Dev/Atmel/RestAPIJavaDocGenerator/DocOut'

$v_convert_params=@(' ', $RestAPIJavaDocGenerator, '-apiXSLTFile', $Restapi , '-classXSLTFile',$RestClass, '-extFilter java,jav','-inDir', $indir,'-logFile', $logdir,'-outDir',$outdir , '-autoGUID')

echo [string]$v_convert_params

& java -jar $v_convert_params


$cmd_d2t_path = 'C:\Program Files (x86)\Trisoft\DITA2Trisoft\10.1\dita2trisoft.exe' 
$v_action = 'action:convertandimport'
$v_reguidize = 'reguidize:yes'
#$v_inputfolder = 'inputfolder:"C:\Dev\Atmel\in"'
$v_inputfolder = $outdir
$v_outputfolder = 'outputfolder:"C:\Dev\Atmel\out"' 

$v_pass_param = @($v_action.ToString(),$v_reguidize.ToString(),$v_inputfolder.ToString(), $v_outputfolder.ToString())


echo [string]$cmd_d2t_path

echo [string]$v_pass_param


& $cmd_d2t_path  $v_pass_param



