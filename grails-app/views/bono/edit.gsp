<html>
<head>
	<meta charset="UTF-8">
	<title>Bono ${bonoInstance.id}</title>
	<r:require module="forms"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="well">
					<h3>Aguinaldo de ${bonoInstance.empleado} ${bonoInstance.ejercicio} 
						(${g.formatDate(date:bonoInstance.fechaInicial)} - ${g.formatDate(date:bonoInstance.fechaFinal)})
						<p>
						<small>
							Salario diario: <g:formatNumber number="${bonoInstance.salario}" type="currency"/> 
							Tipo: ${bonoInstance.empleado.salario.periodicidad}
						</small>
					</h3>
					<g:if test="${ flash.message }">
						<span class="label label-warning text-center">${flash.message}</span>
					</g:if>
				</div>
			</div>
			<g:hasErrors bean="${bonoInstance}">
	            <div class="alert alert-danger">
	                <g:renderErrors bean="${bonoInstance}" as="list" />
	            </div>
	        </g:hasErrors>
		</div><!-- end .row 1 -->
		

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
					<div class="btn-group">
						
					</div>
					
				</div>
			</div>
		</div><!-- end .row 2 -->

		<div class="row">
		
			<g:form  name="updateForm" class="form-horizontal" action="update" method="PUT">
			<div class="row">
			<div class="col-md-6 col-md-offset-2">	
					<g:hiddenField name="id" value="${bonoInstance.id}" />
					<g:hiddenField name="version" value="${bonoInstance.version}" />
				
					<f:with bean="${bonoInstance }">
						<fieldset disabled>
							<f:field property="fechaInicial" widget-class="readOnly"/>
						</fieldset>
						<f:field property="porcentajeBono"  widget-class="form-control " widget-type="number"
							value ="${bonoInstance.porcentajeBono}"/>
						%{-- <f:field property="faltas"  widget-class="form-control" widget-type="text"/>
						<f:field property="permisoEspecial"  widget-class="form-control" widget-type="text"/>
						<f:field property="incapacidades"  widget-class="form-control" widget-type="text"/>
						<f:field property="incapacidadesRTT"  widget-class="form-control" widget-type="text"/>
						<f:field property="incapacidadesRTE"  widget-class="form-control" widget-type="text"/>
						<f:field property="incapacidadesMAT"  widget-class="form-control" widget-type="text"/> --}%
						<f:field property="manual"  widget-class="form-control" />
					</f:with>
			</div>
			</div>	
				<div class="form-group">
					<div class="col-md-offset-2 col-sm-4">
						
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-arrow-left"></span> Bonos
					    </g:link>
						<button type="submit" class="btn btn-default">
							<span class="glyphicon glyphicon-floppy-save"></span> Actualizar
						</button>
					</div>
				</div>

			</g:form>
			
		</div><!-- end .row 3 -->

		

	</div>
	
	<script type="text/javascript">
		$(function(){
			$("form[name='updateForm2']").submit(function(e){
			    
			    
			    //e.preventDefault(); //STOP default action
			});

			//$(".bono").autoNumeric({vMin:'0.00',vMax:100.00});
		});
	</script>
	
	
	
</body>
</html>