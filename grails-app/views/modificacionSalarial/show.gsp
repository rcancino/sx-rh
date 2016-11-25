
<html>
	<head>
		<meta charset="UTF-8">
		
		<title>Modificación ${modificacionInstance.id}</title>
		
		
	</head>
	<body>
		
		    <div class="row wrapper border-bottom white-bg page-heading">
            <div class="col-lg-10">
                <h2>Modificacion Salarial </h2>
                
                <g:if test="${flash.message}">
                    <small><span class="label label-warning ">${flash.message}</span></small>
                </g:if> 
                <g:if test="${flash.error}">
                    <small><span class="label label-danger ">${flash.error}</span></small>
                </g:if> 
            </div>
        </div>

        <div class="wrapper wrapper-content animated fadeInRight">
            <div class="row">
                <div class="col-lg-8">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Modificacion Salarial</h5>
                        </div>
                        <form class="form-horizontal" >  
                            <div class="ibox-content">
                                <lx:errorsHeader bean="${modificacionInstance}"/>
								<g:render template="showForm"/>
                            </div>
                            <div class="ibox-footer">
                                <div class="btn-group">
                                    <lx:backButton/>
                                    <lx:createButton/>
										<lx:printButton action="reporteDeSDI" id="${modificacionInstance.id}"/>
						            <g:if test="${modificacionInstance.calculoSdi}">
										<g:if test="${modificacionInstance.calculoSdi.status!='APLICADO' }">
											<g:link action="aplicar" id="${modificacionInstance.id}" > 
											   <input type="button" value="Aplicar" class="button btn btn-primary btn-outline"/>
											</g:link>
										</g:if>		
									</g:if>
                                    <g:if test="${modificacionInstance.calculoSdi}">
                                        <g:if test="${modificacionInstance.calculoSdi.status!='APLICADO' }">
                                            <g:link action="delete" id="${modificacionInstance.id}" > 
                                               <input type="button" value="Eliminar" class="button btn btn-primary btn-outline"/>
                                            </g:link>
                                        </g:if>     
                                    </g:if>
									
                                </div>                                     
                            </div>
                        </form>     
                    </div>
                </div>
            </div>
        </div>	
	</body>
</html>