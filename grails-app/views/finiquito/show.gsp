
<!doctype html>
<html>
	<head>
		<meta name="layout" content="application">
		<g:set var="entity" value="${finiquitoInstance}" scope="request"/>
		<g:set var="entityName" value="Finiquito" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<g:set var="delete" value="true" scope="request"/>
		<title>Finiquito: ${entity.empleado}</title>
	</head>
	<body>
		<lx:header>
			<h2>Finiquito: ${entity.empleado}</h2>
			<lx:warningLabel/>
		</lx:header>

		<lx:wrapper>
			<div class="col-md-6">
				<lx:ibox>
					<lx:iboxTitle title="Propiedades"></lx:iboxTitle>
				<g:form name="updateForm" action="update" class="form-horizontal" method="PUT">
					<g:hiddenField name="id" value="${entity.id}"/>
                    <g:hiddenField name="version" value="${entity.version}"/> 
                    <div class="ibox-content">
                        <lx:errorsHeader bean="${entity}"/>
                        <f:with bean="${entity}">
							<f:display property="empleado" widget-class="form-control" wrapper="bootstrap3"/>
							<f:display property="baja.fecha" widget-class="form-control" label="Baja" wrapper="bootstrap3"/>
							<f:field property="diasPorPagar" widget-class="form-control" wrapper="bootstrap3"/>
                            <f:field property="montoIntereses" widget-class="form-control" wrapper="bootstrap3"/>
                            <f:field property="tasaInteres" widget-class="form-control" wrapper="bootstrap3"/>
                            <f:field property="sdiOpcion" widget-class="form-control" wrapper="bootstrap3"/>
                            <f:field property="liq" widget-class="form-control" wrapper="bootstrap3"/>
						</f:with>
                    </div>
                    <lx:iboxFooter>
                    	<div class="btn-group">
                            <lx:backButton/>
                            <g:link class="btn btn-success btn-outline" controller="finiquitoDet" action="create" id="${finiquitoInstance.id}">
                                <i class="fa fa-plus"></i> Agregar 
                            </g:link>
                            <g:if test="${editable}">
                                <button id="saveBtn" class="btn btn-primary ">
                                    <i class="fa fa-floppy-o"></i> Actualizar
                                </button>
                            </g:if>
                            <g:if test="${printable}">
                                <lx:printButton id="${entity?.id}"/>
                            </g:if>
                            <g:if test="${delete}">
                                <a href="" class="btn btn-danger " 
                                	data-toggle="modal" 
                                	data-target="#deleteDialog">
                                	<i class="fa fa-trash">
                                	</i> Eliminar
                                </a> 

                            </g:if>
                            <g:link  action="imprimir" class="btn btn-default btn-outline" id="${entity.id}" >
                                <i class="fa fa-print"></i> Imprimir
                            </g:link> 
                                    
                        </div>
                    </lx:iboxFooter>
                </g:form>	
                </lx:ibox>
                <g:render template="partidasPanel"/>
			</div>

			<div class="col-md-6">
				<g:render template="propiedadesPanel"/>
			</div>

            <div class="col-md-8 col-md-offset-2" >
                
            </div>
		</lx:wrapper>
	
		<g:render template="deleteFiniquitoDialog"></g:render>
	
	</div>	
		<script>
			$(function(){
				var res = $('.grid').dataTable({
            		responsive: true,
            		aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
            		"language": {
                	"url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
            		},
            		"dom": 'ft<"clear">lrip',
            		"tableTools": {
                	"sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
            		},
            		"order": []
        		});
			});
		</script>
	</body>
</html>
