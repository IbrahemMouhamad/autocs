// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import * as React from 'react';
import { useState } from 'react';
import Box from '@mui/material/Box';

import {
    useTranslate,
    DashboardMenuItem,
    MenuItemLink,
    MenuProps,
    useSidebarState,
} from 'react-admin';

import icons from '../icons';
import SubMenu from './SubMenu';

type MenuName = 'menuEntities';

const Menu = ({ dense = false }: MenuProps): JSX.Element => {
    const [state, setState] = useState({
        menuEntities: true,
    });
    const translate = useTranslate();
    const [open] = useSidebarState();

    const handleToggle = (menu: MenuName): void => {
        setState((oldState) => ({ ...oldState, [menu]: !oldState[menu] }));
    };

    return (
        <Box
            sx={{
                width: open ? 200 : 50,
                marginTop: 1,
                marginBottom: 1,
                transition: (theme) => theme.transitions.create('width', {
                    easing: theme.transitions.easing.sharp,
                    duration: theme.transitions.duration.leavingScreen,
                }),
            }}
        >
            <DashboardMenuItem />
            <SubMenu
                handleToggle={() => handleToggle('menuEntities')}
                isOpen={state.menuEntities}
                name='menu.entities'
                icon={React.createElement(icons.entities)}
                dense={dense}
            >
                <MenuItemLink
                    to='/datacenters'
                    state={{ _scrollToTop: true }}
                    primaryText={translate('resources.datacenters.name', {
                        smart_count: 2,
                    })}
                    leftIcon={React.createElement(icons.datacenters)}
                    dense={dense}
                />
                <MenuItemLink
                    to='/hosts'
                    state={{ _scrollToTop: true }}
                    primaryText={translate('resources.hosts.name', {
                        smart_count: 2,
                    })}
                    leftIcon={React.createElement(icons.hosts)}
                    dense={dense}
                />
                <MenuItemLink
                    to='/brokers'
                    state={{ _scrollToTop: true }}
                    primaryText={translate('resources.brokers.name', {
                        smart_count: 2,
                    })}
                    leftIcon={React.createElement(icons.brokers)}
                    dense={dense}
                />
                <MenuItemLink
                    to='/vms'
                    state={{ _scrollToTop: true }}
                    primaryText={translate('resources.vms.name', {
                        smart_count: 2,
                    })}
                    leftIcon={React.createElement(icons.vms)}
                    dense={dense}
                />
                <MenuItemLink
                    to='/cloudlets'
                    state={{ _scrollToTop: true }}
                    primaryText={translate('resources.cloudlets.name', {
                        smart_count: 2,
                    })}
                    leftIcon={React.createElement(icons.cloudlets)}
                    dense={dense}
                />
            </SubMenu>
            <MenuItemLink
                to='/configurations'
                state={{ _scrollToTop: true }}
                primaryText={translate('resources.configurations.name', {
                    smart_count: 2,
                })}
                leftIcon={React.createElement(icons.configurations)}
                dense={dense}
            />
            <MenuItemLink
                to='/experiments'
                state={{ _scrollToTop: true }}
                primaryText={translate('resources.experiments.name', {
                    smart_count: 2,
                })}
                leftIcon={React.createElement(icons.experiments)}
                dense={dense}
            />
        </Box>
    );
};

export default Menu;
