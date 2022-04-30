// Copyright (C) 2020-2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import { Admin, Resource } from 'react-admin';
import englishMessages from 'ra-language-english';
import polyglotI18nProvider from 'ra-i18n-polyglot';

import { dataProvider } from './dataProvider';
import * as domainMessages from './i18n';

import { ConfigurationResourceProps } from './configurations';
import {
    DatacenterResourceProps,
    HostResourceProps,
    VMResourceProps,
    CloudletResourceProps,
    BrokerResourceProps,
} from './entities';
import { ExperimentResourceProps } from './experiments';

import { Layout, lightTheme } from './layout';

const messages = {
    en: { ...englishMessages, ...domainMessages.en },
};
const i18nProvider = polyglotI18nProvider((locale) => messages[locale]);

const App = (): JSX.Element => (
    <Admin
        dataProvider={dataProvider}
        i18nProvider={i18nProvider}
        layout={Layout}
        theme={lightTheme}
    >
        <Resource {...DatacenterResourceProps} />
        <Resource {...HostResourceProps} />
        <Resource {...VMResourceProps} />
        <Resource {...CloudletResourceProps} />
        <Resource {...ConfigurationResourceProps} />
        <Resource {...ExperimentResourceProps} />
        <Resource {...BrokerResourceProps} />
    </Admin>
);

export default App;
