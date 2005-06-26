<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

    <P>
    <H2>Owner Information</H2>

    <TABLE border="0">
      <TR><TD>Name</TD><TD><b><c:out value="${model.owner.firstName}"/> <c:out value="${model.owner.lastName}"/></b></TD></TR>
      <TR><TD>Address</TD><TD><c:out value="${model.owner.address}"/></TD></TR>
      <TR><TD>City</TD><TD><c:out value="${model.owner.city}"/></TD></tr>
      <TR><TD>Telephone </TD><TD><c:out value="${model.owner.telephone}"/></TD></TR>
      <TR>
        <TD></TD>
        <TD>
            <FORM method=GET action="<c:url value="/editOwner.htm"/>">
                <INPUT type="hidden" name="ownerId" value="<c:out value="${model.owner.id}"/>"/>
                <INPUT type="submit" value="Edit Owner"/>
            </FORM>
        </TD>
      </TR>
    </TABLE>

    <P>
    <BR>
    <H3>Pets and Visits</H3>

    <c:forEach var="pet" items="${model.owner.pets}">
    <TABLE border="0">
      <TR><TD valign="top">
        <TABLE border="0">
            <TR><TD>Name</TD><TD><b><c:out value="${pet.name}"/></b></TD></TR>
            <TR><TD>Birth Date</TD><TD><fmt:formatDate value="${pet.birthDate}" pattern="yyyy-MM-dd"/></TD></TR>
            <TR><TD>Type</TD><TD><c:out value="${pet.type.name}"/></TD></tr>
            <TR>
                <TD></TD>
                <TD>
                    <FORM method=GET action="<c:url value="/editPet.htm"/>" name="formEditPet<c:out value="${pet.id}"/>">
                        <INPUT type="hidden" name="petId" value="<c:out value="${pet.id}"/>"/>
                        <INPUT type="submit" value="Edit Pet"/>
                    </FORM>
                    <FORM method=GET action="<c:url value="/addVisit.htm"/>" name="formVisitPet<c:out value="${pet.id}"/>">
                        <INPUT type="hidden" name="petId" value="<c:out value="${pet.id}"/>"/>
                        <INPUT type="submit" value="Add Visit"/>
                    </FORM>
                </TD>
            </TR>
        </TABLE>
      </TD><TD valign="top">
        <TABLE border="1">
            <TH>Visit Date</TH><TH>Description</TH>
            <c:forEach var="visit" items="${pet.visits}">
            <TR>
                <TD><fmt:formatDate value="${visit.date}" pattern="yyyy-MM-dd"/></TD>
                <TD><c:out value="${visit.description}"/></TD>
            </TR>
            </c:forEach>
        </TABLE>
        </TD></TR>
    </TABLE>
    <P>
    <BR>
    </c:forEach>

    <FORM method=GET action="<c:url value="/addPet.htm"/>" name="formAddPet">
        <INPUT type="hidden" name="ownerId" value="<c:out value="${model.owner.id}"/>"/>
        <INPUT type="submit" value="Add New Pet"/>
    </FORM>
    <BR>
  
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
