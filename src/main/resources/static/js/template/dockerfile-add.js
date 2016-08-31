$(document).ready(function(){
	var editor_one = CodeMirror.fromTextArea(document.getElementById("dockerFile"), {
        lineNumbers: true,
        matchBrackets: true,
        styleActiveLine: true,
        theme: "ambiance"
    });
	$("#dockerfile").focus();
	
});




