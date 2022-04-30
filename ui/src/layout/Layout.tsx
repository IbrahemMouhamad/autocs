// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import * as React from 'react';
import { Layout, LayoutProps } from 'react-admin';
import AppBar from './AppBar';
import Menu from './Menu';

export default (props: LayoutProps): JSX.Element => (
    <Layout {...props} appBar={AppBar} menu={Menu} />
);
