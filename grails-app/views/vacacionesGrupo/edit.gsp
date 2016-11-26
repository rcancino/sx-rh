<html>
<head>
	<meta charset="UTF-8">
	<title>Vacaciones grupo ${vacacionesGrupoInstance.id}</title>
	<asset:javascript src="forms/forms.js"/>
</head>
<body>

	<lx:header>
		<h2>${vacacionesGrupoInstance.comentario} <small>registro de vacaciones</small></h2>
		<lx:warningLabel bean="${vacacionesGrupoInstance}"/>
		<lx:errorsHeader bean="${vacacionesGrupoInstance}"/>
	</lx:header>

	<div class="row wrapper wrapper-content animated fadeInRight">
		
		<div class="col-md-6">
			<div class="ibox float-e-margins">
				<g:form name="updateForm" action="update" class="form-horizontal" method="PUT">  
					<g:hiddenField name="id" value="${vacacionesGrupoInstance.id}"/>
					<g:hiddenField name="version" value="${vacacionesGrupoInstance.version}"/>

					<div class="ibox-title"> <h5>Propiedades</h5> </div>

					<div class="ibox-content">
						
						<f:with bean="${vacacionesGrupoInstance}">
							<f:field property="comentario" widget-class="form-control" />
							<f:field property="fechaInicial"/>
							<f:field property="fechaFinal"/>
							<f:field  property="calendarioDet" widget-class="form-control"/>
						</f:with>
					</div>

					<div class="ibox-footer">
						<div class="btn-group">
							<lx:backButton action="index" label="Grupos"/>
						    <button type="submit" id="saveBtn" class="btn btn-primary ">
						        <i class="fa fa-floppy-o"></i> Actualizar
						    </button>
						    <lx:deleteButton bean="${vacacionesGrupoInstance}"/>
						</div>	
					</div>
				</g:form>
			</div>
		</div>

		<div class="col-md-6">
			<g:render template="partidasPanel" model="[editable:true]"/>
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

	        $(".numeric").autoNumeric('init',{vMin:'0'},{vMax:'9999'});
	        $(".money").autoNumeric('init',{wEmpty:'zero',mRound:'B',aSign: '$'});
	        $(".tc").autoNumeric('init',{vMin:'0.0000'});
	        $(".porcentaje").autoNumeric('init',{altDec: '%', vMax: '99.99'});

	        $('form[name=updateForm]').submit(function(e){
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