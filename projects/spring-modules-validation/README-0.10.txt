Annotation based client side validation

New in 0.10 is (partial) client side support for your annotation based validation.  This is still experimental, and assumes all the expressions to be in Valang (I am still unclear if/when OGNL is used). The client code does do cross field checks, but the targets must also be on the command object, and be rendered to <INPUT..> tags or similar - how else could it see the other fields! All supported validation annotations are converted to valang equivalents; we reuse the valang client side framework.

Currently supported field annotations:

* @Email
* @Expression
* @Expressions
* @InTheFuture *
* @InThePast *
* @Length
* @Max
* @MaxLength
* @MaxSize
* @Min
* @MinLength
* @MinSize
* @NotBlank
* @NotEmpty
* @NotNull
* @Range
* @RegExp
* @Size

Note that the @InThe(Future|Past) annotations will probably need an implementation of ValangValidator.Rule.prototype.parseDate; see below.

Currently supported Class annotations:

* @Expression
* @Expressions


To setup your client side validation, you will need:
* A command object (bean) annotated with the spring modules validation annotations.
* <%@taglib uri="/WEB-INF/tlds/valang.tld" prefix="vl"%>  in your JSP
* The valang_codebase.js file is either included directly (copy from inside JAR to your webapp) , or by using the <vl:codebase..> tag.
* <vl:beanValidate commandName="[CommandObjectBeanName]" /> inluded on the page

And it should just work!  

If you want to add support for other Annotations, you will need to author a Handler, and instantiate the converter
to include the custom handler.  The tag will automatically look for beans in the webapplicationcontext, and use it
if available.

example:


<bean id="annotationConverter"
    class="org.springmodules.validation.bean.annotation.javascript.taglib.CommandObjectToValangConverter">
    <property name="registerFieldMethodHandlers"  >
        <set>
            <!-- this bean implements Handler -->
            <bean class="com.myCompany.ExampleHandler"/>
            <bean class="com.myCompany.AnotherExampleHandler"/>
        </set>
    </property>
</bean>
 


The codebase has had some updates:

* You can override ValangValidator.prototype.fieldValidationCallback = function(field, isValid, ruleCount); if you want something to happen when the per-field validation happens.  If ruleCount == 0, isValid will be true, as no rules failed!
* You can override ValangValidator.prototype.formValidationCallback = function(field, isValid); if you want something to happen on whole-form validation.
* You should override ValangValidator.Rule.prototype.parseDate = function(dateString, fieldName); which is a javascript dateString -> JS Date function to support your intended date format.  The default implementation uses the JavaScript Date.parse() method, which doesnt support many formats.  Also, note that the field name is passed in so different fields can have differing formats.
* The globalErrorsId and fieldErrorIdSuffix variables have been moved from global namespace into the ValangValidator object, and the logs ID has been made a variable also:
** ValangValidator.prototype.globalErrorsId - the ID for all global (class level) errors to render to
** ValangValidator.prototype.fieldErrorIdSuffix - the suffix to append to the field name to construct the ID of the error message target.
** ValangValidator.Logger.logId - the ID to inject log messages into.  
** Can be set with the <vl:codebase logId="myLog" globalErrorsId="myGlobalErrors" fieldErrorIdSuffix="_myErrSuffix"/> tag if it is used.
* The default logger has a huge performance improvement, now uses nested lists instead of simply appending to the innerHTML.









