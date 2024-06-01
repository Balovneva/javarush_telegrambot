package com.github.javarushcommunity.jrtb.javarushclient;

import com.github.javarushcommunity.jrtb.javarushclient.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.javarushcommunity.jrtb.javarushclient.dto.GroupInfoType.TECH;

@DisplayName("Integration-level testing for JavaRushGroupClientImplTest")
public class JavaRushGroupClientTest {

    public static final String JAVARUSH_API_PATH = "https://javarush.ru/api/1.0/rest";
    private final JavaRushGroupClient groupClient =
            new JavaRushGroupClientImpl(JAVARUSH_API_PATH);

    @Test
    public void shouldProperlyGetGroupsWithEmptyArgs() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder().build();

        //when
        List<GroupDiscussionInfo> groupList = groupClient.getGroupDiscussionList(args);

        //then
        Assertions.assertNotNull(groupList);
        Assertions.assertFalse(groupList.isEmpty());
    }

    @Test
    public void shouldProperlyGetWithOffSetAndLimit() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder()
                .offset(1)
                .limit(3)
                .build();

        //when
        List<GroupDiscussionInfo> groupList = groupClient.getGroupDiscussionList(args);

        //then
        Assertions.assertNotNull(groupList);
        Assertions.assertEquals(3, groupList.size());
    }

    @Test
    public void shouldProperlyGetGroupCount() {
        //given
        GroupsCountRequestArgs args = GroupsCountRequestArgs.builder().build();

        //when
        Integer groupCount = groupClient.getGroupCount(args);

        //then
        Assertions.assertEquals(28, groupCount);
    }

    @Test
    public void shouldProperlyGetGroupTECHCount() {
        //given
        GroupRequestArgs args = GroupRequestArgs.builder()
                .type(TECH)
                .build();

        //when
        List<GroupDiscussionInfo> groupList = groupClient.getGroupDiscussionList(args);

        //then
        Assertions.assertNotNull(groupList);
        Assertions.assertEquals(7, groupList.size());
    }

    @Test
    public void shouldProperlyGetGroupById() {
        //given
        Integer androidGroupId = 16;

        //when
        GroupDiscussionInfo groupById = groupClient.getGroupById(androidGroupId);

        //then
        Assertions.assertNotNull(groupById);
        Assertions.assertEquals(16, groupById.getId());
        Assertions.assertEquals(TECH, groupById.getType());
        Assertions.assertEquals("android", groupById.getKey());
    }
}
