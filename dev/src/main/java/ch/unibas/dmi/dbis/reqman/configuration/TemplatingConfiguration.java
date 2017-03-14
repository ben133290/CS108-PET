package ch.unibas.dmi.dbis.reqman.configuration;

import ch.unibas.dmi.dbis.reqman.common.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: write JavaDoc
 *
 * @author loris.sauter
 */
class TemplatingConfiguration {

    private String extension;

    private Map<String, String> templates;

    @JsonIgnore
    public static final String REQUIREMENT = "requirement";
    @JsonIgnore
    public static final String MILESTONE = "milestone";
    @JsonIgnore
    public static final String CATALOGUE = "catalogue";
    @JsonIgnore
    public static final String PROGRESS = "progress";
    @JsonIgnore
    public static final String GROUP_MILESTONE = "group-milestone";
    @JsonIgnore
    public static final String GROUP = "group";
    @JsonIgnore
    public static final String TEMPLATE = "template";

    @JsonIgnore
    public static final Map<String, String> DEFAULT_TEMPLATING_CONFIGURATION = generateDefaultTemplatesMap();

    public TemplatingConfiguration(){
        templates = new HashMap<>();
    }

    public TemplatingConfiguration(String extension){
        this();
        this.extension = extension;
    }

    private TemplatingConfiguration(String extension, Map<String, String> templates){
        this();
        this.extension = extension;
        // Don't clear the map, since it cannot have any values in it.
        this.templates.putAll(templates);
    }

    public String getExtension() {
        return extension;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    /**
     * Validates the templates property and fixes missing entries, by setting them to their default value.
     * @return {@code true} if a fix was made, otherwise {@code false}
     */
    public boolean validateTemplatesAndFix(){
        int nbFixes = 0;
        if(templates.isEmpty() ){
            templates.putAll(generateDefaultTemplatesMap() );
            nbFixes++;
        }else{
            nbFixes += validateAndFixEntry(REQUIREMENT);
            nbFixes += validateAndFixEntry(MILESTONE);
            nbFixes += validateAndFixEntry(CATALOGUE);
            nbFixes += validateAndFixEntry(PROGRESS);
            nbFixes += validateAndFixEntry(GROUP_MILESTONE);
            nbFixes += validateAndFixEntry(GROUP);
        }

        return nbFixes != 0;
    }

    /**
     * Validates and fixes the entry of the templates map.
     * @param key The key to validate for
     * @return An integer > 0 if a fix was made, 0 otherwise
     */
    private int validateAndFixEntry(String key){
        if(!templates.containsKey(key) ){
            templates.put(key, DEFAULT_TEMPLATING_CONFIGURATION.get(key));
            return 1;
        }else{
            return 0;
        }
    }

    private static final Map<String, String> generateDefaultTemplatesMap(){
        HashMap<String,String> map = new HashMap<>();

        map.put(REQUIREMENT, StringUtils.concatWithPeriodDelimeter(REQUIREMENT, TEMPLATE));
        map.put(MILESTONE, StringUtils.concatWithPeriodDelimeter(MILESTONE, TEMPLATE));
        map.put(CATALOGUE, StringUtils.concatWithPeriodDelimeter(CATALOGUE, TEMPLATE));
        map.put(PROGRESS, StringUtils.concatWithPeriodDelimeter(PROGRESS, TEMPLATE));
        map.put(GROUP_MILESTONE, StringUtils.concatWithPeriodDelimeter(GROUP_MILESTONE, TEMPLATE));
        map.put(GROUP, StringUtils.concatWithPeriodDelimeter(GROUP, TEMPLATE));

        return map;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TemplatingConfiguration{");
        sb.append("extension='").append(extension).append('\'');
        sb.append(", templates=").append(templates);
        sb.append('}');
        return sb.toString();
    }
}
