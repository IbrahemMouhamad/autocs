// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { ResourceProps } from 'react-admin';

import icons from '../icons';

import EntityList from './EntityList';
import EntityCreate from './EntityCreate';
import EntityEdit from './EntityEdit';
import BasicConfigurationForm from './BasicConfigurationForm';
import AdvancedConfigurationForm from './AdvancedConfigurationForm';
import EntityAssignmentForm from './EntityAssignmentForm';
import ConfigurationFormTab from './ConfigurationFormTab';
import { EntityInput } from './EntityConfigurationForm';
import EntityDetails from './EntityDetails';

const CommonEntityResourceProps = {
    list: EntityList,
    create: EntityCreate,
    edit: EntityEdit,
};

const DatacenterResourceProps: ResourceProps = {
    ...CommonEntityResourceProps,
    name: 'datacenters',
    icon: icons.datacenters,
};

const HostResourceProps: ResourceProps = {
    ...CommonEntityResourceProps,
    name: 'hosts',
    icon: icons.hosts,
};

const VMResourceProps: ResourceProps = {
    ...CommonEntityResourceProps,
    name: 'vms',
    icon: icons.vms,
};

const CloudletResourceProps: ResourceProps = {
    ...CommonEntityResourceProps,
    name: 'cloudlets',
    icon: icons.cloudlets,
};

export {
    DatacenterResourceProps,
    HostResourceProps,
    VMResourceProps,
    CloudletResourceProps,
    BasicConfigurationForm,
    AdvancedConfigurationForm,
    EntityList,
    EntityAssignmentForm,
    ConfigurationFormTab,
    EntityInput,
    EntityDetails,
};
