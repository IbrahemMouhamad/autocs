// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { DataProvider } from 'react-admin';

import defaultDataProvider, { baseUrl } from './defaultDataProvider';

const resources = {
    configurations: 'configurations',
    datacenters: 'entities/datacenters',
    hosts: 'entities/hosts',
    vms: 'entities/vms',
    cloudlets: 'entities/cloudlets',
    brokers: 'entities/brokers',
    experiments: 'experiments',
};

const dataProviderWrapper: DataProvider = {
    getList: (resource: string, params) => defaultDataProvider.getList(resources[resource], params),
    getOne: (resource: string, params) => defaultDataProvider.getOne(resources[resource], params),
    getMany: (resource, params) => defaultDataProvider.getMany(resources[resource], params),
    getManyReference: (resource: string, params) => defaultDataProvider.getManyReference(resources[resource], params),
    create: (resource: string, params) => defaultDataProvider.create(resources[resource], params),
    update: (resource: string, params) => defaultDataProvider.update(resources[resource], params),
    updateMany: (resource: string, params) => defaultDataProvider.updateMany(resources[resource], params),
    delete: (resource: string, params) => defaultDataProvider.delete(resources[resource], params),
    deleteMany: (resource: string, params) => defaultDataProvider.deleteMany(resources[resource], params),
};

export {
    defaultDataProvider,
    dataProviderWrapper as dataProvider,
    baseUrl,
};
