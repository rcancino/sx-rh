<f:with bean="${modificacionInstance}">
	<f:field property="empleado" >
		<g:hiddenField id="empleadoId" name="empleado.id"/>
		<g:field id="empleadoField" name="empleadoNombre" class="form-control" type="text" required="" />
	</f:field>
	<f:field property="fecha" widget-class="form-control" />
	<f:field property="tipo" widget-class="form-control" />

	<f:field property="sdiAnterior" >
		<input name="sdiAnterior" type="text" class="form-control" id = "sdiAnteriorField" readonly>
	</f:field>
 
	<f:field property="salarioAnterior" >
		<input name="salarioAnterior" type="text" class="form-control" id = "salarioAnteriorField"readonly   >
	</f:field>


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
	
});
</script>