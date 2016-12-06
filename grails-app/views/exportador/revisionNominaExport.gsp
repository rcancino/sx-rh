<%@ page import="com.luxsoft.sw4.rh.CalendarioDet" %>
<html>
<head>
<meta charset="UTF-8">
<meta name="layout" content="exportadores"/>
<title>Exportacion</title>

<script>
$(function(){
			console.log('Autocompletando calendarios');
			$("#calendarioDetField").autocomplete({
				source:'<g:createLink controller="calendarioDet" action="getCalendariosAsJSON"/>',
				minLength:1,
				select:function(e,ui){
					$("#calendarioFieldId").val(ui.item.id);
				}
			});
		});
</script>
</head>
<body>


		


	<content tag="reporteTitle">
		Revision de Nomina

	</content>
	
	<content tag="reportForm">
		<g:hasErrors bean="${reportCommand}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${reportCommand}" as="list" />
            </div>
        </g:hasErrors>
		<div class="col-sm-12">
		
		<g:form action="generarArchivoNomina" class="form-horizontal">
			<fieldset>
				<legend> Parámetros</legend>	
				
				<div class="form-group">
  						<label for="comentarioField" class="col-sm-3">Calendario</label>
    					<div class="col-sm-9">
							<g:hiddenField id="calendarioFieldId" name="calendarioDet"  />
							<input 
								id="calendarioDetField" 
								type="text" 
								name="calendarioField"  
								class="form-control " 
								value="" 
								placeholder="Seleccione un calendario">
							</input>
    					</div>
  					</div>

			</fieldset>
			<div class="form-group">
		    	<div class="col-sm-offset-2 col-sm-3">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-cog"></span> Ejecutar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		</div>
	</content>


	
</body>



</html>