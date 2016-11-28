<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Alta de PTU</title>
	<g:set var="entity" value="${ptuInstance}" scope="request" />
	<g:set var="entityName" value="Alta de PTU" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
	
</head>
<body>

		<content tag="header">
			Alta de  PTU
			
		</content>

		<content tag="formTitle">
			
		</content>

		<content tag="formFields">
			<f:with bean="${ptuInstance}">

					<f:field property="ejercicio" >
						<input name="ejercicio" type="text" class="form-control" value="${session.ejercicio}" readonly>
					</f:field>
				<f:field property="remanente" input-class="form-control" widget-type="text"/>
				<f:field property="monto" input-class="form-control" widget-type="text"/>
				<f:field property="factor" input-class="form-control" widget-type="text"/>
			</f:with>
		</content>




	<div class="container-fluid">
		
		<div class="row">
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Parámetros</h3>
					</div>
					<div class="panel-body">	
						<g:hasErrors bean="${ptuInstance}">
							<div class="alert alert-danger alert-dismissable">
								<ul>
									<g:eachError var="err" bean="${ptuInstance}">
										<li><g:message error="${err}"/></li>
									</g:eachError>
								</ul>
							</div>
						</g:hasErrors>
						<g:form action="save" 
							role="form" class="form-horizontal" >
							<fieldset>
							<f:with bean="${ptuInstance}">
								<f:field property="ejercicio" 
									input-class="form-control" input-type="text" input-readonly='readonly'
									value="${ptuInstance.ejercicio}"/>
								<f:field property="remanente" input-class="form-control" input-type="text"/>
								<f:field property="monto" input-class="form-control" input-type="text"/>
								<f:field property="factor" input-class="form-control" input-type="text"/>
							</f:with>
							</fieldset>
							<div class="form-group">
						    	<div class="col-sm-offset-9 col-sm-2">
						      		<button type="submit" class="btn btn-default">
						      			<span class="glyphicon glyphicon-floppy-save"></span> Salvar
						      		</button>
						    	</div>
						  	</div>
						</g:form>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
		
		
	</div>
	
	<r:script>
		$(function(){
			$(".numerico").autoNumeric();
		});
	</r:script>
</body>
</html>