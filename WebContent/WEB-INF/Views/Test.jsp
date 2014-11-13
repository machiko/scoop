<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<link href="../css/bootstrap.min.css" rel="stylesheet">
<link href="../css/style.css" rel="stylesheet">
<title>MMSeg Demo</title>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="../js/jquery-1.11.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../js/bootstrap.min.js"></script>
<script type="text/javascript">
$(function() {
    $("#mmseg-send").click(function() {
    	var content = $("textarea[name=content]").val();
    	var mmsegRowData = "";
    	var ckipRowData = "";
    	
    	// mmseg
	    $.post("/Scoop/api/mmseg", {
	    	content: content
	    }, function(result) {
	    	for (var i = 0; i < result.mmseg.length; i++) {
	    		mmsegRowData += '\
	    		<tr><td>' + i + '</td>\
	    		<td>' + result.mmseg[i] + '</td>\
	    		</tr>\
	    		';	    		
	    	}
    	   $("#result-mmseg tr:eq(0)").after(mmsegRowData);
	        console.log(result.mmseg);
	    }, "json");
	    
    	// ckip
        $.post("/Scoop/api/ckip", {
            content: content
        }, function(result) {
        	for (var i = 0; i < result.word.length; i ++) {
        		ckipRowData += '\
        		<tr><td>' + i + '</td>\
        		<td>' + result.word[i] + '</td>\
        		<td>' + result.speech[i] + '</td>\
        		</tr>\
        		';
        	}
        	$("#result-ckip tr:eq(0)").after(ckipRowData);
            console.log(result);
        }, "json");
    });
});
</script>
</head>
<body>
    <header>
	    <div class="page-header">
		  <h1>Segment Word Demo <small>中文分詞系統</small></h1>
		</div>
	    <div class="container-fluid">
			<form role="form">
			  <div class="form-group">
		        <textarea class="form-control" name="content" rows="5"></textarea>
			  </div>
		      <button type="button" class="btn btn-primary btn-lg" id="mmseg-send">送出</button>
			</form>
			<hr>
	    </div>
    </header>
    <div class="container-fluid" id="segment-result">
        <div class="row">
            <div class="col-md-12 text-center"><h3>分詞結果</h3></div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <table class="table table-striped table-bordered" id="result-mmseg">
                    <caption>mmseg</caption>
	                <tr>
	                    <th>#</th>
	                    <th>字詞</th>
	                </tr>
				</table>
            </div>
            <div class="col-md-6">
                <table class="table table-striped table-bordered" id="result-ckip">
                    <caption>CKIP</caption>
                    <tr>
                        <th>#</th>
                        <th>字詞</th>
                        <th>詞性</th>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    
	
	
</body>
</html>