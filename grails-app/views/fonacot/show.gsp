<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="showForm"/>
	<g:set var="entity" value="${fonacotInstance}" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
	Prestamo FONACOT ${fonacotInstance.empleado}
</content>

<content tag="formTitle">FONACOT ${fonacotInstance.empleado}</content>

<content tag="form">
	<f:with bean="${fonacotInstance }">
		<f:display property="numeroDeCredito" wrapper="bootstrap3"/>
		<f:display property="numeroDeFonacot" wrapper="bootstrap3"/>
		<f:display property="importe" wrapper="bootstrap3"/>
		<f:display property="retencionMensual" wrapper="bootstrap3"/>
		<f:display property="retencionDiaria" wrapper="bootstrap3"/>
		<f:display property="totalAbonos" wrapper="bootstrap3"/>
		<f:display property="saldo" wrapper="bootstrap3"/>
		<f:display property="activo" wrapper="bootstrap3"/>
	</f:with>
	<legend> Abonos </legend>
	%{-- <g:render template="abonosGrid"/> --}%
</content>

<content tag="footer">
	<div class="btn-group">
		<lx:backButton/>
		<lx:createButton/>
		<lx:editButton id="${fonacotInstance.id}"/>
		<a class="btn btn-danger btn-outline" 
                                            data-toggle="modal" data-target="#deleteDialog"><i class="fa fa-trash"></i> Eliminar</a> 
		<g:jasperReport
  			jasper="EstadoDeCuentaFonacot"
  			format="PDF"
  			name="Estado de Cuenta">
  			<g:hiddenField name="ID" value="${fonacotInstance.id}"/>
		</g:jasperReport>

	</div>
	<g:render template="/common/deleteDialog" bean="${fonacotInstance}"/>
</content>
	
	<content tag="operaciones">
		<ul class="nav nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Prestamos
  			    </g:link>
  			</li>
  			<li><g:link action="edit" id="${fonacotInstance.id}">
  					<span class="glyphicon glyphicon-pencil"></span> Editar
  			    </g:link>
  			</li>
  			<li><g:link action="delete" id="${fonacotInstance.id}"
  					onclick="return confirm('Eliminar el registro de FONACOT');">
  					<span class="glyphicon glyphicon-trash"></span> Eliminar
  			    </g:link>
  			</li>
  			<li>
				<g:jasperReport
          			jasper="EstadoDeCuentaFonacot"
          			format="PDF"
          			name="Estado de Cuenta">
          			<g:hiddenField name="ID" value="${fonacotInstance.id}"/>
 				</g:jasperReport>
			</li>
		</ul>
	</content>
	
	
	
	
	
</body>
</html>