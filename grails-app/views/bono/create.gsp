<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Alta de bono anual</title>
	<g:set var="entity" value="${bonoAnualInstance}" scope="request" />
	<g:set var="entityName" value="Calculo de bono anual" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

	<content tag="header">
		Alta de aguinaldo
	</content>

	<content tag="formTitle">
		Calculo de bono anual
	</content>
	<content tag="formFields">
		<f:with bean="${bonoAnualInstance }">
			<g:hiddenField name="ptu" value="${bonoAnualInstance.ptu.id}"/>
			<f:field property="ejercicio" widget-class="form-control" wrapper="bootstrap3" widget-disabled="true"/>
			<f:field widget-id="ptuAnterior"
				property="ptuAnterior" widget-class="form-control money"  
				widget-label="PTU Anterior ${session.ejercicio}" widget-disabled="true" wrapper="bootstrap3" widget-type="text"/>
			<f:field property="porcentaje" widget-class="form-control" wrapper="bootstrap3"  widget-label="Variación (%)"/>
			
			<f:field property="incremento" 
				widget-class="form-control money" widget-type="text"
				wrapper="bootstrap3" 
				widget-disabled="true" 
				widget-label="Importe Var" />
			
			<f:field property="ptuAnteriorActualizada" 
				widget-class="form-control money" 
				widget-label="PTU Ant Actualizada" 
				wrapper="bootstrap3" widget-type="text"
				widget-disabled="true"/>
			<f:field property="ptuActual" widget-class="form-control money" wrapper="bootstrap3" widget-disabled="true" widget-type="text"/>
			<f:field property="monto" widget-class="form-control money" wrapper="bootstrap3" widget-type="text"/>
			

		</f:with>
		<script type="text/javascript">
			$(function(){
				$(".money").autoNumeric('init', {wEmpty:'zero', mRound:'B', aSign: '$' , vMin:'-99999999999'});
				$("#porcentaje").blur( function() {
					var porcentaje = $(this).val();
					var ptuAnterior = $('#ptuAnterior').autoNumeric('get');
					var ptuActual = $('#ptuActual').autoNumeric('get');
					var incremento = ptuAnterior * (porcentaje / 100);
					$('#incremento').autoNumeric('set',incremento);
					
					var ptuAnteriorAct = +ptuAnterior + (incremento);
					var monto = ptuAnteriorAct - ptuActual

					console.log('Calculando icremento :', porcentaje);
					console.log('PTU Anterior :', ptuAnterior);
					console.log('Incremento :', incremento);
					console.log('PTU Anterior Act:', ptuAnteriorAct);

					$('#ptuAnteriorActualizada').autoNumeric('set',ptuAnteriorAct);
					$('#monto').autoNumeric('set', monto);

				});
			});
		</script>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Lista de aguinaldos
  			    </g:link>
  			</li>
  			<li><g:link action="create">
  					<span class="glyphicon glyphicon-plus"></span> Nuevo
  			    </g:link>
  			</li>
		</ul>
	</content>
	
	
	<content tag="form">
		
		<g:hasErrors bean="${bonoAnualInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${bonoAnualInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="save">
			
			<f:with bean="${bonoAnualInstance}">
				<f:field property="ejercicio" input-class="form-control"/>
			</f:with>
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		
		

	</content>
	
</body>
</html>