<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Empleados</title>
	
</head>
<body>
	<content tag="header">
		<h3>Empleados activos</h3>
	</content>


	<div class="row toolbar">
        
         
         <div class="btn-group">
             <lx:refreshButton action=""/>
             <lx:createButton/>
         </div>
        
     </div>

	
	<content tag="gridPanel" >
		<g:render template="empleadosGrid"/>
	</content>
</body>
</html>