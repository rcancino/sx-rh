<g:hiddenField id="${property}Id" name="${property}.id" value="${value?.id}" />
<input 
	id="${property}" 
	type="text" 
	name="${property}Field"  
	class="form-control " 
	value="${value}" 
	placeholder="Seleccione un empleado">
</input>

<script type="text/javascript">
	$(function(){
		$("#${property}").autocomplete({
			source:'<g:createLink controller="empleadoRest" action="getEmpleados"/>',
			minLength:1,
			select:function(e,ui){
				$("#${property}Id").val(ui.item.id);
			},
		});
	});
</script>