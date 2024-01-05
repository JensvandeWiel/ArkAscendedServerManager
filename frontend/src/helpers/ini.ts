// getVal is a function that takes a gus object, a section, a key, and an index, and returns a string. This function is used to handle when anything is undefined, if so it creates it.
export function getVal(gus: { [key: string]: { [key: string]: Array<string> } }, section: string, key: string, index: number = 0): string {

    if (!gus[section]) {
        gus[section] = {};
    }

    if (!gus[section][key]) {
        gus[section][key] = [];
    }

    if (!gus[section][key][index]) {
        gus[section][key][index] = '';
        return gus[section][key][index];
    }

    return gus[section][key][index];
}