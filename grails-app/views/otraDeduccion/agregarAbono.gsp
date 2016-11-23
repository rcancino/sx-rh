<html>
<head>
	<meta charset="UTF-8">
	<title>Abono otra deduccion</title>
	<asset:javascript src="forms/forms.js"/>
</head>	
<body>
<lx:header>
	<h2>
		Otra deduccion: ${otraDeduccionInstance.id} <small>(${otraDeduccionInstance.empleado})</small>
	</h2>
</lx:header>

<div class="row wrapper wrapper-content animated fadeInRight">
	<div class="col-md-8 col-md-offset-2">
		<lx:ibox>
			<lx:iboxTitle title="Propiedades"/>
			<g:form name="createForm" action="salvarAbono" class="form-horizontal" method="POST" >  
			    <g:hiddenField name="otraDeduccionId" value="${otraDeduccionInstance.id}"/>
			    <lx:iboxContent>
				    <f:with bean="${otraDeduccionAbonoInstance}">
				    	<f:field property="fecha" />
				    	<f:field property="importe" widget="money"/>
				    	<f:field property="comentario" widget-class="form-control"/>
				    </f:with>
			    </lx:iboxContent>
			    <lx:iboxFooter>
			    	<div class="btn-group">
		    			<lx:backButton action="edit" id="${otraDeduccionInstance.id}"/>
		    		    <button id="saveBtn" class="btn btn-primary ">
		    		        <i class="fa fa-floppy-o"></i> Salvar
		    		    </button>
			    	</div>
			    	
			    </lx:iboxFooter>
			</g:form>
		</lx:ibox>
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

        $('form[name=createForm]').submit(function(e){
            console.log("Desablidatndo submit button....");

            var button=$("#saveBtn");
            button.attr('disabled','disabled')
            .html('Procesando...');

            $(".money,.porcentaje,.numeric,.tc",this).each(function(index,element){
              var val=$(element).val();
              var name=$(this).attr('name');
              var newVal=$(this).autoNumeric('get');
              $(this).val(newVal);
            });
            //e.preventDefault(); 
            return true;
        });

    });
</script>  
	
	
</body>
</html>