<g:applyLayout name="application">
<html>
<head>
	<title><g:layoutTitle/></title>
	<g:layoutHead/>
</head>
</html>
<body>
<lx:header>
	<h2>Módulo de exportación de información </h2>
	<lx:warningLabel/>
</lx:header>

<div class="row wrapper  animated fadeInRight">
	<div class="col-md-4 toolbar">
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title"> Layouts</h4>
			</div>
			<div id="collapse1" class="panel-collapse collapse in">
				<nav:menu class="nav nav-pills nav-stacked" scope="app/exportadores" />
			</div>
		</div>

	</div>

	<div class="col-md-8 toolbar">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title"> <g:pageProperty name="page.reporteTitle"/></h4>
			</div>
			<div class="panel-body">
				<g:pageProperty name="page.reportForm"/>	
			</div>
		</div>
	</div>

	<script type="text/javascript">
                $(function(){
                    $('.date').bootstrapDP({
                        format: 'dd/mm/yyyy',
                        keyboardNavigation: false,
                        forceParse: false,
                        autoclose: true,
                        todayHighlight: true
                    });
                    $('.chosen-select').chosen();
                    $(".numeric").autoNumeric('init',{vMin:'0'},{vMax:'9999'});
                    $(".money").autoNumeric('init',{wEmpty:'zero',mRound:'B',aSign: '$'});
                    $(".tc").autoNumeric('init',{vMin:'0.0000'});
                    $(".porcentaje").autoNumeric('init',{altDec: '%', vMax: '99.99'});

                    

                });
	</script> 

</div>

		
		
	</body>
</g:applyLayout>