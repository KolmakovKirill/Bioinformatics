package project.urfu.vk.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.GetMembersSort;
import com.vk.api.sdk.objects.users.Fields;

public class VkService
{
    private static final Long APP_ID = 1L;
    private static final String CODE = "";

    private final VkApiClient client;
    private final UserActor userActor;

    public VkService()
    {
        TransportClient transportClient = new HttpTransportClient();
        this.client = new VkApiClient(transportClient);
        client.setVersion("5.81");

        this.userActor = new UserActor(APP_ID, CODE);
    }

    public List<VkMemberResponse> getGroupByName(String name) throws ClientException, JsonProcessingException
    {
        List<VkMemberResponse> result = new ArrayList<>();

        for (int j = 1; j <= 4; j++)
        {
            String json = client.groups().getMembers(userActor)
                    .groupId(name)
                    .sort(GetMembersSort.ID_ASC)
                    .count(1000)
                    .offset(j * 1000 - 1000)
                    .fields(Fields.BDATE, Fields.CITY)
                    .executeAsString();

            final JsonNode node = new ObjectMapper().readTree(json);
            ArrayNode itemsArrayNode = (ArrayNode)node.get("response").get("items");
            for (int i = 0; i < itemsArrayNode.size(); i++)
            {
                JsonNode jsonNode = itemsArrayNode.get(i);
                VkMemberResponse vkMemberResponse = new VkMemberResponse();
                vkMemberResponse.setId(jsonNode.get("id").asLong(0));
                vkMemberResponse.setFirst_name(jsonNode.get("first_name").asText());
                vkMemberResponse.setLast_name(jsonNode.get("last_name").asText());
                vkMemberResponse.setBdate(jsonNode.has("bdate") ? jsonNode.get("bdate").asText() : null);

                if (jsonNode.has("city"))
                {
                    jsonNode = jsonNode.get("city");
                    VkCityResponse vkCityResponse = new VkCityResponse();
                    vkCityResponse.setId(jsonNode.get("id").asLong(0));
                    vkCityResponse.setTitle(jsonNode.get("title").asText());
                    vkMemberResponse.setCity(vkCityResponse);
                }

                result.add(vkMemberResponse);
            }
        }

        return result;
    }
}
