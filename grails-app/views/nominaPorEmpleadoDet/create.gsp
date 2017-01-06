<html>
<head>
	<meta charset="UTF-8">
	
	<asset:javascript src="forms/forms.js"/>
	<title>Alta de percepción / deducción</title>
</head>
<body>
	<g:set var="ne" value="${nominaPorEmpleadoDetInstance.parent}" scope="request"/>
	<lx:header>
		<g:link controller="nominaPorEmpleado" action="edit" id="${ne.id}">
			<h2>
				Percepción/Deducción para  nomina: ${ne.nomina.folio} ${ne.nomina.tipo} ${ne.nomina.periodicidad}
				<small>( ${ne.empleado})</small>
			</h2>
		</g:link>
	</lx:header>

	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
		    <div class="col-lg-10 col-lg-offset-1">

		    	<g:form name="createForm" action="save" class="form-horizontal" method="POST" >
		    		<g:hiddenField name="parent" value="${ne.id}"/>
		    		<lx:ibox>
		    			<lx:iboxTitle title="Deducción / Percepción"/>
		    			
		    			<lx:errorsHeader bean="${nominaPorEmpleadoDetInstance}"/>
		    		
		    			<lx:iboxContent>
		    				<f:with bean="${nominaPorEmpleadoDetInstance }">
		    					<f:field property = "concepto" widget-class="form-control"/>
		    					<f:field property="importeGravado" widget="money" widget-class="form-control"/>
		    					<f:field property="importeExcento" widget="money" widget-class="form-control"/>
		    					<f:field property="comentario" widget-class="form-control"/>
		    				</f:with>
		    			</lx:iboxContent>

		    			<lx:iboxFooter>
		    				<div class="btn-group">
		                        <button id="saveBtn" class="btn btn-primary ">
		                            <i class="fa fa-floppy-o"></i> Salvar
		                        </button>
		                        <lx:backButton controller="nominaPorEmpleado" action="edit" id="${ne.id}" label="Cancelar"/>
		                    </div>
		    			</lx:iboxFooter>
		    		</lx:ibox>
		    	</g:form>
		    	
		        
		    </div>
		</div>
	</div>
	
	
	<script type="text/javascript">
	    $(function(){
	        
	        $('.chosen-select').chosen();
	        $(".money").autoNumeric('init',{wEmpty:'zero',mRound:'B',aSign: '$'});
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