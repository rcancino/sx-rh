<html>
<head>
	<meta charset="UTF-8">
	<title>Solicitud vacaciones ${vacacionesInstance.id}</title>
	<g:set var="control" value="${vacacionesInstance.control}" scope="request" />
	<asset:javascript src="forms/forms.js"/>
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2>${vacacionesInstance.empleado} <small>registro de vacaciones</small></h2>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	        <g:if test="${flash.error}">
	            <small><span class="label label-danger ">${flash.error}</span></small>
	        </g:if> 
	    </div>
	</div>

	<div class="row wrapper wrapper-content animated fadeInRight">
		
		<div class="col-md-6">
			<div class="ibox float-e-margins">
				<g:form name="updateForm" action="update" class="form-horizontal" method="PUT">  
					<g:hiddenField name="id" value="${vacacionesInstance.id}"/>
					<g:hiddenField name="version" value="${vacacionesInstance.version}"/>

					<div class="ibox-title"> <h5>Propiedades</h5> </div>

					<div class="ibox-content">
		
						<f:display bean="control" property="id" widget-class="form-control" />
						<f:display bean="control" property="diasDisponibles" widget-class="form-control" />
						<f:display bean="control" property="vigencia" widget-class="form-control" />
						
						<f:with bean="${vacacionesInstance}">
							<f:display property="empleado" widget-class="form-control" />
							<f:field property="comentario" widget-class="form-control" />
							<f:field widget-id="pagadoField" property="pg" widget-class="form-control"
								widget-autocomplete="off" 
								label="Pagadas"/>
							<f:field widget-id="diasPagadosField" 
								property="diasPagados" 
								widget-class="form-control" label="# Dias "
						 		widget-disabled="${!vacacionesInstance.diasPagados}"/>

							<f:field  property="calendarioDet" widget-class="form-control"/>
							%{-- <div class="form-group"><label for="calendarioField" class="col-sm-2 control-label">Calendario</label>
								<div class="col-sm-10">
									<g:select id="calendarioField" 
										class="form-control"  
										name="calendarioDet.id" 
										value="${vacacionesInstance?.calendarioDet?.id}"
										from="${periodos}" 
										optionKey="id" 
										optionValue="${{it.calendario.tipo+' '+it.folio+' ( '+it.inicio.format('yyyy-MMM-dd')+' al '+it.fin.format('yyyy-MMM-dd')+ ' )'}}"
										disabled="${vacacionesInstance.diasPagados<=0}?'':'disabled'"
										noSelection="['':'Seleccione un calendario']"
										autocomplete="off"
								/>
							</div> 
						</div>
						--}%
						<f:field input-id="cierreAnualField" 
							property="cierreAnual" 
							input-class="form-control" label="Cierre anual"
						 	/>
							
							
						</f:with>
					</div>
					<div class="ibox-footer">
						<div class="btn-group">
							<lx:backButton action="show" id="${vacacionesInstance.id}" />
						    <button type="submit" id="saveBtn" class="btn btn-primary ">
						        <i class="fa fa-floppy-o"></i> Actualizar
						    </button>
						    <lx:deleteButton bean="${vacacionesInstance}"/>
						</div>	
					</div>
				</g:form>
			</div>
		</div>

		<div class="col-md-6">
			<g:render template="fechasPanel"/>
		</div>
	</div>

	<script type="text/javascript">
		$(function(){
			$("#pagadoField").on('change',function(e){
				if ($(this).is(':checked') == false){
					$('#diasPagadosField').prop('disabled', true);
					$('#calendarioDet').prop('disabled', true);
					$('#addFecha').prop('disabled', false);
				}else{
					$('#diasPagadosField').prop('disabled', false);
					$('#calendarioDet').prop('disabled', false);
					$('#addFecha').prop('disabled', true);
				}
				console.log('Vacaciones pagadas: '+$(this).is(':checked'));
			});
			var dp=$("#diasPagadosField").val();
			
			if(dp!=='undefined' && dp<=0){
				$('#calendarioDet').prop('disabled', true);
			}
			
		});
	</script>
	
</body>
</html>