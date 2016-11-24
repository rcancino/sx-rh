<%@page expressionCodec="none" %>
<r:require modules="jquery-ui,forms"/>
		
	
				<f:with bean="${modificacionInstance}">
					<f:field property="empleado" >
						<g:hiddenField id="empleadoId" name="empleado.id"/>
						<g:field id="empleadoField" name="empleadoNombre" class="form-control" type="text" required="" />
					</f:field>
					<f:field property="fecha" widget-class="form-control" />
					<f:field property="tipo" widget-class="form-control" />

					<f:field property="sdiAnterior" >
						<input type="text" class="form-control" id = "sdiAnteriorField" disabled>
					</f:field>

					<f:field property="salarioAnterior" widget-class="form-control" 
							input-readonly="" 
							input-id="salarioAnteriorField"
							input-type="text"/>

					<f:field input-id="salarioNuevo" 
							property="salarioNuevo" 
							widget-class="form-control numerico" 
							input-type="text"
							input-autocomplete="off"/>
					
					<f:field property="comentario" widget-class="form-control"/>
				</f:with>

<script>

$(function(){
	$("#empleadoField").autocomplete({
			source:'<g:createLink controller="empleadoRest" action="getEmpleadosConSalario"/>',
			minLength:3,
			select:function(e,ui){
				console.log('Valor seleccionado: '+ui.item.id);
				$("#empleadoId").val(ui.item.id);
				$("#salarioAnteriorField").val(ui.item.salarioDiario);
				$("#sdiAnteriorField").val(ui.item.sdi);
			}
	});
	$(".numerico").autoNumeric();
	/*
	$("#salarioNuevo").blur(function(){
		var sdi=$(this).val();
		var empleadoId=$("#empleadoId").val();
		var salarioNuevo=$("#salarioNuevo").autoNumeric('get');
		if(empleadoId!=="" && salarioNuevo!==null){
			
			$('#submitBtn').attr('disabled', 'disabled');
			var fecha=$('#fecha').val();
			console.log('Nuevo salario: '+sdi+ ' Fecha: '+fecha);
			jQuery.getJSON(
				'<g:createLink controller="empleadoRest" action="calcularSdi"/>',
				{empleadoId:empleadoId,fecha:fecha,salarioNuevo:salarioNuevo},function(data){

				}
			).done(function(data){
				$('#sdiNuevoField').val(data.sdi);
				if(data.sdi>0){
					$('#submitBtn').removeAttr("disabled");
				}
			});
		}
		
	});
	*/
});
</script>