// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { Theme } from '@mui/material/styles';

declare module '@mui/styles/defaultTheme' {
    // eslint-disable-next-line @typescript-eslint/no-empty-interface
    interface DefaultTheme extends Theme { }
}

const lightTheme = {
    themeName: 'light',
    palette: {
        background: {
            default: '#fafafa',
        },
    },
    components: {
    },
};

export default lightTheme;
