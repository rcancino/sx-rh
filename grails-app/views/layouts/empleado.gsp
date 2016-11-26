<g:applyLayout name="application">
<html>
<head>
	<meta charset="UTF-8">
	<title>Empleado: ${empleadoInstance?.perfil?.numeroDeTrabajador}</title>
</head>
<body>

	<div class="row wrapper border-bottom white-bg page-heading">
	    <div class="col-lg-10">
	        <h2> ${empleadoInstance.nombre}</h2>
	        <ol class="breadcrumb">
        	    <li><g:link controller="empleado"  action="generales">Generales</g:link></li>
        	    <li><g:link controller="empleado" action="generales">Datos personales</g:link></li>
        	    <li><g:link controller="empleado" action="perfil">Perfil</g:link></li>
        	    <li><g:link controller="empleado" action="salario">Salario</g:link></li>
        	    <li><g:link controller="empleado" action="seguridadSocial">Seguridad social</g:link></li>
        	    <li><g:link controller="empleado" action="contactos">Contactos</g:link></li>
        	    <li><g:link controller='pensionAlimenticia' action='edit'>Pensión</g:link></li>
        	    <li><g:link controller='pensionAlimenticia' action='edit'>Expediente</g:link></li>
	        </ol>
	        <g:if test="${flash.message}">
	            <small><span class="label label-warning ">${flash.message}</span></small>
	        </g:if> 
	    </div>
	</div>

	<div class="row wrapper wrapper-content animated fadeInRight">
		<div class="col-md-12">
			<div class="ibox float-e-margins">
				<lx:iboxTitle title="Folio: ${empleadoInstance.id}"/>
				<div class="ibox-content">
					
					<g:hasErrors bean="${empleadoInstance}">
						<div class="alert alert-danger alert-dismissable">
							<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							<ul>
								<g:eachError var="err" bean="${empleadoInstance}">
									<li><g:message error="${err}"/></li>
								</g:eachError>
							</ul>
						</div>
					</g:hasErrors>
					<g:pageProperty name="page.content"/>
				</div>
				%{-- <div class="ibox-footer">
					<g:pageProperty name="page.actions"/>
					<button class="btn btn-default" data-toggle="modal" data-target="#searchForm">
						<span class="glyphicon glyphicon-search"></span> Buscar
					</button>
				</div> --}%
			</div>
		</div>
	</div>

	<script type="text/javascript">
	    $(function(){

	        $('.date').bootstrapDP({
	            format: 'dd/mm/yyyy',
	            keyboardNavigation: false,
	            forceParse: false,
	            autoclose: true,
	            todayHighlight: true,

	        });
	        

	    });
	</script>  
	
	
</body>
</html>
</g:applyLayout>