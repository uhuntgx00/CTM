<%@ page import="mil.ebs.ctm.Account" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
		<title>Current Authentication</title>
	</head>
	<body>
        <div id="list-account" class="content scaffold-list" role="main">
            <table>
            	<thead>
                    <tr class="account-view">
                        <th class="account-view">Name</th>
                        <th class="account-view">Value</th>
                    </tr>
            	</thead>
            	<tbody>
                    <tr class="account-view">
                   		<td>Name</td>
                   		%{--<td class="no_underline"><g:link action="show" id="${accountInstance.id}">${auth.name}</g:link></td>--}%
                   	</tr>
                    <tr class="account-view">
                        <td>Authorities</td>
                        %{--<td>${auth.authorities}</td>--}%
                    </tr>
                    <tr class="account-view">
                        <td>Credentials</td>
                        %{--<td>${auth.credentials}</td>--}%
                    </tr>
                    <tr class="account-view">
                        <td>Details</td>
                        %{--<td>${auth.details}</td>--}%
                    </tr>
                    <tr class="account-view">
                        <td>Principal</td>
                        %{--<td>${auth.principal}</td>--}%
                    </tr>
            	</tbody>
            </table>
		</div>

	</body>
</html>
